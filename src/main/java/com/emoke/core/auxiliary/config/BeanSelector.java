package com.emoke.core.auxiliary.config;//package com.emoke.core.pangu.config;
//
//import com.emoke.core.pangu.NetAuxiliary;
//import com.emoke.core.pangu.annotation.EnableNetAuxiliary;
//import lombok.SneakyThrows;
//import org.springframework.context.annotation.ImportSelector;
//import org.springframework.core.annotation.AnnotationAttributes;
//import org.springframework.core.type.AnnotationMetadata;
//import java.util.Map;
//
//public class BeanSelector implements ImportSelector {
//
//    @SneakyThrows
//    @Override
//    public String[] selectImports(AnnotationMetadata annotationMetadata) {
//
//        System.out.println("12$$$$$$$$$$$$$$$");
//        // annotationMetadata.getAnnotatedMethods()
//        AnnotationAttributes attributes = AnnotationAttributes
//                .fromMap(annotationMetadata.getAnnotationAttributes(EnableNetAuxiliary.class.getName(), true));
//        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
//            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
//            if ("basePackage".equals(entry.getKey())) {
//                NetAuxiliary netAuxiliary =new NetAuxiliary(entry.getValue().toString());
//                return new String[]{netAuxiliary.getClass().getName()};
//            }
//        }
//        System.out.println("12$$$$$$$$$$$$$$$:" + attributes.size());
//        return new String[0];
//    }
//}
