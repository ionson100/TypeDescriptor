import java.lang.annotation.*;
import java.util.*;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@interface NameE {
    String name() default "ok";
}

public class Main {

    public static void main(String[] args) {
        {
            User user1 = new User();
            TypeDescriptor.addAnnotation(user1, new NameE() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return NameE.class;
                }

                @Override
                public String name() {
                    return "User1";
                }
            }, new NameE() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return NameE.class;
                }

                @Override
                public String name() {
                    return "User++1";
                }
            });
            TypeDescriptor.addAnnotation(user1, new NameE() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return NameE.class;
                }

                @Override
                public String name() {
                    return "User1++";
                }
            });
            Collection<Class<NameE>> dd = TypeDescriptor.getAnnotations(user1, NameE.class);
            for (Object a : dd) {
                String str = ((NameE) a).name();
                System.out.println(str);
            }
        }
        {
            User user1 = new User();
            TypeDescriptor.addAnnotation(user1, new NameE() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return NameE.class;
                }

                @Override
                public String name() {
                    return "User2";
                }
            });
            Collection<Class<NameE>> dd = TypeDescriptor.getAnnotations(user1, NameE.class);
            for (Object a : dd) {
                String str = ((NameE) a).name();
                System.out.println(str);

            }
        }
    }
}

class User {
}

final class TypeDescriptor {
    private static final Map<Object, Collection<Annotation>> df;

    static {
        df = Collections.synchronizedMap(new WeakHashMap<Object, Collection<Annotation>>());
    }

    private static Collection<Annotation> read(Object o) {
        return df.get(o);
    }

    private static void write(Object o, final Annotation annotation) {
        df.put(o, Collections.synchronizedList(new ArrayList<Annotation>(){{add(annotation);}}));
    }

    private static void postWrite(Object o, Annotation annotation) {
        df.get(o).add(annotation);
    }


    public static void addAnnotation(Object object, Annotation... annotations) {
        if (annotations == null) {
            throw new IllegalArgumentException("annotations as null, this ass.. ");
        }
        for (Annotation annotation : annotations) {
            if (read(object) != null) {
                postWrite(object, annotation);
            } else {
                write(object, annotation);
            }
        }
    }

    public static Collection<Annotation> getAnnotations(Object o) {
        return innerGetAnnotations(o);
    }

    public static Collection getAnnotations(Object o, Class aClass) {
        Worker w = new Worker(aClass);
        return w.getAnnotation(o);
    }

    private static Collection<Annotation> innerGetAnnotations(Object o) {
        Collection<Annotation> res = new ArrayList<Annotation>();
        Collection<Annotation> compatible = read(o);
        if (compatible != null) {
            res.addAll(compatible);
        }
        Collections.addAll(res, o.getClass().getAnnotations());
        return res;
    }

    private static class Worker<T> {
        private final T t;

        public Worker(T t) {
            this.t = t;
        }

        public Collection<T> getAnnotation(Object o) {
            List<T> res = new ArrayList<T>();
            for (Annotation a : innerGetAnnotations(o)) {
                if (a.annotationType() == t) {
                    res.add((T) a);
                }
            }
            return res;
        }
    }
}
