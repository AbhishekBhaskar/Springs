$output.java($primaryKey.packageName, "${primaryKey.type}_")##

$output.require("javax.persistence.metamodel.StaticMetamodel")##
$output.require("javax.persistence.metamodel.SingularAttribute")##

@StaticMetamodel(${primaryKey.type}.class)
public abstract class $output.currentClass {
#foreach ($attribute in $primaryKey.attributes)
#if ($velocityCount == 1)
    // pk attributes
#end
$output.require($attribute)##
    public static volatile SingularAttribute<${primaryKey.type}, $attribute.type> $attribute.var;
#end
}
