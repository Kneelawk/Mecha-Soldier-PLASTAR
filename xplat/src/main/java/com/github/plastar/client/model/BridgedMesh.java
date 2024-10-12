package com.github.plastar.client.model;

import de.javagl.jgltf.model.AccessorFloatData;
import de.javagl.jgltf.model.AccessorShortData;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import dev.engine_room.flywheel.api.model.IndexSequence;
import dev.engine_room.flywheel.api.model.Mesh;
import dev.engine_room.flywheel.api.vertex.MutableVertexList;

import dev.engine_room.flywheel.api.vertex.VertexList;
import dev.engine_room.flywheel.lib.memory.MemoryBlock;
import dev.engine_room.flywheel.lib.model.ModelUtil;
import dev.engine_room.flywheel.lib.vertex.PosTexNormalVertexView;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4fc;

import org.lwjgl.system.MemoryUtil;

public class BridgedMesh implements Mesh {
    
    private final Vector4fc boundingSphere;
    private final Indices indices;
    private final VertexList vertices;

    public BridgedMesh(MeshPrimitiveModel primitive, Matrix4f transform) {
        var positionAccessor = primitive.getAttributes().get("POSITION");
        var positionData = ((AccessorFloatData) positionAccessor.getAccessorData());
        
        var normalAccessor = primitive.getAttributes().get("NORMAL");
        var normalData = ((AccessorFloatData) normalAccessor.getAccessorData());
        
        var uvAccessor = primitive.getAttributes().get("TEXCOORD_0");
        var uvData = ((AccessorFloatData) uvAccessor.getAccessorData());
        
        var indexData = ((AccessorShortData) primitive.getIndices().getAccessorData());
        
        indices = new Indices(indexData);
        vertices = buildVertices(transform, positionData, normalData, uvData);
        boundingSphere = ModelUtil.computeBoundingSphere(vertices);
    }

    private VertexList buildVertices(Matrix4f transformMatrix, AccessorFloatData positionData,
                                     AccessorFloatData normalData, AccessorFloatData uvData) {
        var normalMatrix = transformMatrix.normal(new Matrix3f());

        var vertexCount = positionData.getNumElements();
        
        var vertices = new PosTexNormalVertexView();
        var block = MemoryBlock.mallocTracked((long) vertexCount * PosTexNormalVertexView.STRIDE);
        vertices.ptr(block.ptr());
        
        for (int i = 0; i < vertexCount; i++) {
            var pos = new Vector3f(positionData.get(i, 0), positionData.get(i, 1), positionData.get(i, 2));
            transformMatrix.transformPosition(pos);
            vertices.x(i, pos.x);
            vertices.y(i, pos.y);
            vertices.z(i, pos.z);
            
            var normal = new Vector3f(normalData.get(i, 0), normalData.get(i, 1), normalData.get(i, 2));
            normalMatrix.transform(normal);
            vertices.normalX(i, normal.x);
            vertices.normalY(i, normal.y);
            vertices.normalZ(i, normal.z);
            
            vertices.u(i, uvData.get(i, 0));
            vertices.v(i, uvData.get(i, 1));
        }
        
        vertices.ptr(block.ptr());
        vertices.vertexCount(vertexCount);
        vertices.nativeMemoryOwner(block);
        
        return vertices;
    }

    @Override
    public int vertexCount() {
        return vertices.vertexCount();
    }

    @Override
    public void write(MutableVertexList vertexList) {
        vertices.writeAll(vertexList);
    }

    @Override
    public IndexSequence indexSequence() {
        return indices;
    }

    @Override
    public int indexCount() {
        return indices.indexData.getNumElements();
    }

    @Override
    public Vector4fc boundingSphere() {
        return boundingSphere;
    }
    
    private class Indices implements IndexSequence {
        private final AccessorShortData indexData;

        public Indices(AccessorShortData indexData) {
            this.indexData = indexData;
        }

        @Override
        public void fill(long ptr, int count) {
            var writtenCount = Math.min(count, indexCount());
            for (int i = 0; i < writtenCount; i++) {
                MemoryUtil.memPutInt(ptr + i * 4L, indexData.getInt(i, 0));
            }
        }
    }
}
