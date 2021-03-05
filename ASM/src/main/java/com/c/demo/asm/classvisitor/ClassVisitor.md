## ClassVisitor

必须按照该类的Javadoc中指定的以下顺序调用ClassVisitor类的方法：
```text
visit visitSource? visitOuterClass? ( visitAnnotation | visitAttribute )*
( visitInnerClass | visitField | visitMethod )*
visitEnd
```

即：必须先调用访问，然后最多调用一次 visitSource，然后最多一次调用 visitOuterClass，
然后按任意顺序按任意顺序访问任意多个 visitAnnotation 和 visitAttribute，然后按
任意顺序任意多次调用 访问 visitInnerClass，visitField 和 visitMethod，并通过单
次调用visitEnd终止。