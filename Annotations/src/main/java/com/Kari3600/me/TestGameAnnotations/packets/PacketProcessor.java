package com.Kari3600.me.TestGameAnnotations.packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

@AutoService(Processor.class)
@SupportedAnnotationTypes("*")
public class PacketProcessor extends AbstractProcessor {

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private TypeMirror getFieldType(CreatePacket.Field field) {
        try {
            field.type();
            return null;
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("Processor init");
        Map<String,CreatePacket> packets = new HashMap<>();
        Map<String,List<String>> packetHierarchy = new HashMap<>();

        for (Element element : roundEnv.getElementsAnnotatedWith(CreatePackets.class)) {
            for (CreatePacket createPacket : element.getAnnotation(CreatePackets.class).value()) {
                packets.put(createPacket.name(), createPacket);
                if (createPacket.name().equals("")) continue;
                if (!packetHierarchy.containsKey(createPacket.parent())) {
                    packetHierarchy.put(createPacket.parent(), new ArrayList<>());
                }
                List<String> parentChildren = packetHierarchy.get(createPacket.parent());
                parentChildren.add(createPacket.name());
            };
        }

        for (CreatePacket createPacket : packets.values()) {
            String className = createPacket.name();

            TypeSpec.Builder classBuilder = TypeSpec.classBuilder("Packet"+className)
                                                    .addModifiers(Modifier.PUBLIC);

            if (!className.equals("")) {
                classBuilder.superclass(ClassName.bestGuess("Packet"+createPacket.parent()));
            }

            boolean hasChildren = packetHierarchy.get(className) != null;

            MethodSpec.Builder reader = MethodSpec.methodBuilder("read")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(ObjectInputStream.class,"ois")
                .addException(IOException.class)
                .addException(ClassNotFoundException.class)
                .returns(ClassName.bestGuess("Packet"+className));

            if (hasChildren)
                reader.addStatement("byte packetID = ois.readByte()");

            MethodSpec.Builder writer = MethodSpec.methodBuilder("write")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ObjectOutputStream.class,"oos")
                .addException(IOException.class);
            
            if (!className.equals("")) {
                writer.addStatement("super.write(oos)");
                writer.addStatement("oos.writeByte((byte) $L)",packetHierarchy.get(createPacket.parent()).indexOf(className));
            }

            for (CreatePacket.Field field : createPacket.fields()) {
                TypeMirror fieldTypeMirror = getFieldType(field);
                String fieldTypeName = fieldTypeMirror.toString();
                ClassName fieldType = ClassName.bestGuess(fieldTypeName);

                String fieldName = field.name();
                System.out.println("Processing field: " + fieldName + " of type " + fieldType);

                FieldSpec fieldSpec = FieldSpec.builder(fieldType, fieldName, Modifier.PRIVATE).build();
                classBuilder.addField(fieldSpec);

                reader.addStatement("$L $L = ($L) ois.readObject()", fieldType, fieldName, fieldType);
                writer.addStatement("oos.writeObject($L)",fieldName);

                MethodSpec getter = MethodSpec.methodBuilder("get" + capitalize(fieldName))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(fieldType)
                    .addStatement("return $L", fieldName)
                    .build();
                classBuilder.addMethod(getter);

                MethodSpec setter = MethodSpec.methodBuilder("set" + capitalize(fieldName))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(ClassName.bestGuess("Packet"+className))
                    .addParameter(fieldType, fieldName)
                    .addStatement("this.$L = $L", fieldName, fieldName)
                    .addStatement("return this")
                    .build();
                classBuilder.addMethod(setter);
            }

            if (hasChildren) {
                reader.addStatement("Packet$L packet",className);
                reader.addCode("switch (packetID) {\n");
                for (int id=0;id<packetHierarchy.get(className).size();++id) {
                    reader.addCode("    case $L:\n",id);
                    reader.addStatement("    packet = Packet$L.read(ois)",packetHierarchy.get(className).get(id));
                    reader.addStatement("    break");
                }
                reader.addCode("    default:\n");
                reader.addStatement("    throw new IOException(\"Invalid packet ID\")");
                reader.addStatement("}");
            } else {
                reader.addStatement("Packet$L packet = new Packet$L()",className,className);
            }

            for (CreatePacket.Field field : createPacket.fields()) {
                String fieldName = field.name();
                reader.addStatement("packet.$L = $L", fieldName, fieldName);
            }

            reader.addStatement("return packet");
            
            classBuilder.addMethod(reader.build());
            classBuilder.addMethod(writer.build());

            String classPackage= "com.Kari3600.me.TestGameCommon.packets";
            try {
                JavaFile javaFile = JavaFile.builder(classPackage, classBuilder.build()).indent("    ").build();
                JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(classPackage+".Packet"+className);
                System.out.println("Creating file: " + javaFileObject.toUri().getPath());
                try (Writer owriter = javaFileObject.openWriter()) {
                    javaFile.writeTo(owriter);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    
}
