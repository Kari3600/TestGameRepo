package com.Kari3600.me.TestGameClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.HashMap;

import com.Kari3600.me.TestGameClient.util.Object3D;
import com.Kari3600.me.TestGameClient.util.TextureInputStream;
import com.Kari3600.me.TestGameClient.util.Triangle;
import com.Kari3600.me.TestGameCommon.util.Vector3;
import com.Kari3600.me.TestGameClient.util.Vertex;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.javagl.jgltf.impl.v2.Accessor;
import de.javagl.jgltf.impl.v2.BufferView;
import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.Image;
import de.javagl.jgltf.impl.v2.Material;
import de.javagl.jgltf.impl.v2.Mesh;
import de.javagl.jgltf.impl.v2.MeshPrimitive;
import de.javagl.jgltf.impl.v2.TextureInfo;

public class ModelLoader {
    private static HashMap<String,Object3D> models = new HashMap<String,Object3D>();
    enum primitiveType {
        SIGNED_BYTE(5120,1),
        UNSIGNED_BYTE(5121,1),
        SIGNED_SHORT(5122,2),
        UNSIGNED_SHORT(5123,2),
        UNSIGNED_INT(5125,4),
        FLOAT(5126,4);

        public int code;
        public int byteLength;

        public static primitiveType fromCode(int code) {
            for (primitiveType a : primitiveType.values()) {
                if (a.code == code) {
                    return a;
                }
            }
            return null;
        }

        private primitiveType(int code, int byteLength) {
            this.code=code;
            this.byteLength=byteLength;
        }
    }

    private static ByteBuffer getBufferData(ByteBuffer bin, BufferView bufferView) {
        //System.out.println("Reading from buffer nr. "+bufferView.getBuffer());
        bin.position(bufferView.getByteOffset());
        ByteBuffer slice = bin.slice();
        slice.limit(bufferView.getByteLength());
        return slice;
    }

    private static ByteBuffer getBufferData(ByteBuffer bin, BufferView bufferView, Accessor accessor) {
        //System.out.println("Reading from buffer nr. "+bufferView.getBuffer());
        bin.position(bufferView.getByteOffset()+accessor.getByteOffset());
        ByteBuffer slice = bin.slice();
        slice.limit(bufferView.getByteLength());
        //slice.limit(accessor.getCount()*primitiveType.fromCode(accessor.getComponentType()).byteLength);
        return slice;
    }

    public static TextureInputStream getTextureIS(String name) {
        try {
            File file = new File("src/main/resources/"+name+".png");
            return (TextureInputStream) file.toURI().toURL().openStream();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } 
    }

    public static Object3D getObject3D(String name) {
        if (models.containsKey(name)) return models.get(name).clone();
        models.put(name, createObject3D(name));
        return models.get(name).clone();
    }

    private static Object3D createObject3D(String name) {
        switch (name) {
            case "Braum":
                return ModelLoader.getObject3DfromGLB("Braum",0).movePivot(new Vector3(-4500,0,-2000));
            case "Boom":
                return ModelLoader.getObject3DfromGLB("Braum",1).movePivot(new Vector3(-4500,0,-2000));
            default:
                return null;
        }
    }

    public static Object3D getObject3DfromGLB(String name, int MeshID) {
        try {
            HashSet<Triangle> triangles = new HashSet<Triangle>();
            // Specify the path to your .glb file
            File glbFile = new File("Client/src/main/resources/"+name+".glb");

            // Load the glTF model
            InputStream stream = glbFile.toURI().toURL().openStream();
            byte[] Type = new byte[4];
            stream.read(Type);
            System.out.println(new String(Type));
            byte[] Header = new byte[8];
            stream.read(Header);
            IntBuffer buffer = ByteBuffer.wrap(Header).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
            System.out.println(buffer.get());
            System.out.println(buffer.get());
            GlTF gltf = null;
            ByteBuffer bin = null;
            for (int x=0;x<2;x++) {
                //System.out.println("For chunk:");
                int ChunkSize = ByteBuffer.wrap(stream.readNBytes(4)).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().get();
                String ChunkType = new String(stream.readNBytes(4));
                //System.out.println(String.format("ChunkSize: %d, ChunkType: %s",ChunkSize,ChunkType));
                //stream.mark(ChunkSize);
                if (ChunkType.equals("JSON")) {
                    InputStream stream2 = glbFile.toURI().toURL().openStream();
                    stream2.skip(12+8);
                    long skippedtotal = stream.skip(ChunkSize);
                    //System.out.println("Skipped: "+skippedtotal);
                    while (skippedtotal != ChunkSize) {
                        skippedtotal += stream.skip(ChunkSize-skippedtotal);
                        //System.out.println("Skipped: "+skippedtotal);
                    }
                    
                    gltf = new ObjectMapper().readValue(stream2, GlTF.class);
                } else {
                    System.out.println("Bin Found");
                    if (gltf != null) {
                        bin = ByteBuffer.wrap(stream.readAllBytes());
                    }
                }
            }
            stream.close();

            // Iterate through meshes
            Mesh mesh = gltf.getMeshes().get(MeshID);
            //System.out.println("Found mesh");

                // Iterate through primitives (each primitive can be a set of triangles, lines, etc.)
                for (MeshPrimitive primitive : mesh.getPrimitives()) {
                    // We're interested in triangles
                    int mode = primitive.getMode();
                    System.out.println(mode);
                    if (mode != 4) continue;

                    Material material = gltf.getMaterials().get(primitive.getMaterial());
                    TextureInfo texture = material.getPbrMetallicRoughness().getBaseColorTexture();
                    @SuppressWarnings("unchecked")
                    LinkedHashMap<String,ArrayList<?>> textureParams = (LinkedHashMap<String,ArrayList<?>>)texture.getExtensions().get("KHR_texture_transform");
                    System.out.println(textureParams.get("offset").get(0).getClass());
                    double[] textureOffset = new double[2];
                    double[] textureScale = new double[2];
                    for (int c=0;c<2;c++) {
                        Object obj1 = textureParams.get("offset").get(0);
                        Object obj2 = textureParams.get("scale").get(0);
                        System.out.println(obj1.getClass());
                        System.out.println(obj2.getClass());
                        if (obj1 instanceof Double) {
                            textureOffset[c] = (double) obj1;
                        } else if (obj1 instanceof Integer) {
                            textureOffset[c] = (double)(int) obj1;
                        }
                        if (obj2 instanceof Double) {
                            textureScale[c] = (double) obj2;
                        } else if (obj2 instanceof Integer) {
                            textureScale[c] = (double)(int) obj2;
                        }
                    }
                    Image image = gltf.getImages().get(texture.getIndex());
                    BufferView imageBufferView = gltf.getBufferViews().get(image.getBufferView());
                    ByteBuffer imageBuffer = getBufferData(bin, imageBufferView);

                    // Get the indices accessor
                    Accessor indicesAccessor = gltf.getAccessors().get(primitive.getIndices());

                    // Get the buffer view and buffer for indices
                    BufferView indicesBufferView = gltf.getBufferViews().get(indicesAccessor.getBufferView());
                    ByteBuffer indicesBuffer = getBufferData(bin,indicesBufferView,indicesAccessor);
                    int[] indices = new int[indicesAccessor.getCount()];

                    switch (primitiveType.fromCode(indicesAccessor.getComponentType())) {
                        case UNSIGNED_SHORT:
                        ShortBuffer shortBuffer = indicesBuffer.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
                        shortBuffer.rewind();
                        //System.out.println(String.format("There are %d indices (%d triangles)",indices.length,indices.length/3));
                        for (int i = 0; i < indicesAccessor.getCount(); i++) {
                            indices[i] = shortBuffer.get();
                            //System.out.println(String.format("indices[%d] = %d",i,indices[i]));
                        }
                        break;

                        default:
                        IntBuffer intBuffer = indicesBuffer.asIntBuffer();
                        intBuffer.rewind();
                        //System.out.println(String.format("There are %d indices (%d triangles)",indices.length,indices.length/3));
                        for (int i = 0; i < indicesAccessor.getCount(); i++) {
                            indices[i] = intBuffer.get();
                            //System.out.println(String.format("indices[%d] = %d",i,indices[i]));
                        }
                        break;
                    }

                    // Get the POSITION attribute accessor
                    Accessor positionAccessor = gltf.getAccessors().get(primitive.getAttributes().get("POSITION"));

                    // Get the buffer view and buffer for positions
                    BufferView positionBufferView = gltf.getBufferViews().get(positionAccessor.getBufferView());
                    ByteBuffer positionBuffer = getBufferData(bin,positionBufferView,positionAccessor);

                    // Read positions
                    FloatBuffer floatBuffer = positionBuffer.order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
                    floatBuffer.rewind();

                    Accessor texCoordAccessor = gltf.getAccessors().get(primitive.getAttributes().get("TEXCOORD_0"));

                    // Get the buffer view and buffer for positions
                    BufferView texCoordBufferView = gltf.getBufferViews().get(positionAccessor.getBufferView());
                    ByteBuffer texCoordBuffer = getBufferData(bin,texCoordBufferView,texCoordAccessor);

                    // Read positions
                    FloatBuffer floatBuffer2 = texCoordBuffer.order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
                    floatBuffer2.rewind();

                    //System.out.println(String.format("There are %d Vectors",positionAccessor.getCount()));
                    //System.out.println(primitiveType.fromCode(positionAccessor.getComponentType()));
                    Vertex[] vertices = new Vertex[positionAccessor.getCount()];
                    for (int i = 0; i < positionAccessor.getCount(); i++) {
                        vertices[i] = new Vertex(new Vector3(floatBuffer.get(),floatBuffer.get(),floatBuffer.get()),(textureOffset[0]+floatBuffer2.get())*textureScale[0],(textureOffset[1]+floatBuffer2.get())*textureScale[1]);
                        //System.out.println(String.format("positions[%d] = V3(%f|%f|%f)",i, positions[i].x, positions[i].y, positions[i].z));
                        floatBuffer.get(new float[10]);
                        floatBuffer2.get(new float[11]);
                    }

                    // Now, construct triangles
                    for (int i = 0; i < indices.length; i += 3) {
                        int index0 = indices[i];
                        int index1 = indices[i + 1];
                        int index2 = indices[i + 2];

                        // Now you have the coordinates of the triangle's vertices
                        triangles.add(new Triangle(vertices[index0],vertices[index1],vertices[index2],new float[]{0.5F,0.5F,0.5F}));
                    }
                    return new Object3D(triangles, new TextureInputStream(imageBuffer));
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
