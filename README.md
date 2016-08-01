# TypeDescriptor
add annotations to an object in runtime
```java
@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
@interface NameE {
    String name() default "ok";
}

public class Main {

    public static void main(String[] args) {
        {
            User user1=new User();
            TypeDescriptor.addAnnotation(user1,new NameE(){
                @Override
                public Class<? extends Annotation> annotationType() {
                    return NameE.class;
                }
                @Override
                public String name() {
                    return "User1";
                }
            },new NameE(){
                @Override
                public Class<? extends Annotation> annotationType() {
                    return NameE.class;
                }
                @Override
                public String name() {
                    return "User++1";
                }
            });
            TypeDescriptor.addAnnotation(user1,new NameE(){
                @Override
                public Class<? extends Annotation> annotationType() {
                    return NameE.class;
                }
                @Override
                public String name() {
                    return "User1++";
                }
            });
            Collection<Class<NameE>> dd=TypeDescriptor.getAnnotations(user1,NameE.class);
            for (Object a:dd ) {
                String str=((NameE)a).name();
                System.out.println(str);
            }
        }
        {
            User user1=new User();
            TypeDescriptor.addAnnotation(user1,new NameE(){
                @Override
                public Class<? extends Annotation> annotationType() {
                    return NameE.class;
                }
                @Override
                public String name() {
                    return "User2";
                }
            });
            Collection<Class<NameE>> dd=TypeDescriptor.getAnnotations(user1,NameE.class);
            for (Object a:dd ) {
                String str=((NameE)a).name();
                System.out.println(str);
            }
        }
    }
}

class User{
}
```
