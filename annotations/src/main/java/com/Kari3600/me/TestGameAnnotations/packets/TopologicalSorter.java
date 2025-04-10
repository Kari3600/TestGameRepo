package com.Kari3600.me.TestGameAnnotations.packets;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.*;

public class TopologicalSorter {

    ProcessingEnvironment processingEnv;
    Set<TypeElement> annotatedClasses;

    Map<String, List<TypeElement>> graph = new HashMap<>();
    Map<String, TypeElement> classMap = new HashMap<>();

    Set<String> visited = new HashSet<>();
    List<TypeElement> sorted = new ArrayList<>();

    private void dfs(String className) {
        if (visited.contains(className)) return;
        visited.add(className);

        List<TypeElement> children = graph.getOrDefault(className, List.of());
        for (TypeElement child : children) {
            dfs(child.getQualifiedName().toString());
        }

        sorted.add(classMap.get(className));
    }



    public List<TypeElement> sort() {
        for (TypeElement clazz : annotatedClasses) {
            String className = clazz.getQualifiedName().toString();
            TypeMirror superclass = clazz.getSuperclass();
            String superName = processingEnv.getTypeUtils().erasure(superclass).toString();

            if (!classMap.containsKey(superName)) {
                dfs(className);
            }
        }
        return sorted;
    }

    public TopologicalSorter(Set<TypeElement> annotatedClasses, ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        this.annotatedClasses = annotatedClasses;
        for (TypeElement clazz : annotatedClasses) {
            classMap.put(clazz.getQualifiedName().toString(), clazz);
        }

        for (TypeElement clazz : annotatedClasses) {
            TypeMirror superclass = clazz.getSuperclass();

            if (superclass.getKind() == TypeKind.NONE) continue;

            String superName = processingEnv.getTypeUtils().erasure(superclass).toString();
            if (classMap.containsKey(superName)) {
                graph.computeIfAbsent(superName, k -> new ArrayList<>()).add(clazz);
            }
        }
    }

}
