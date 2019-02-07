$output.java($entity.dto)##

/**
 * Simple DTO for ${entity.model.type}.
 */
#if($entity.isRoot())
public class $output.currentClass {
#else
$output.require($entity.parent.dto)##
public class $output.currentClass extends $entity.parent.dto.type {
#end
#if($entity.isRoot() && $entity.primaryKey.isComposite())
$output.require($entity.primaryKey)##
    // Composite primary key
    public $entity.primaryKey.type $entity.primaryKey.var;
#end
#foreach ($attribute in $entity.nonCpkAttributes.list)
#if((!$attribute.isInFk() || $attribute.isSimplePk()) && !$attribute.isBinary())
$output.require($attribute)##
    public $attribute.type $attribute.var;
#end
#end
#foreach ($relation in $entity.xToOne.list)
    public $relation.toEntity.dto.type $relation.to.var;
#end
#foreach ($relation in $entity.manyToMany.list)
$output.require("java.util.List");
    public List<$relation.toEntity.dto.type> $relation.to.vars;
#end

#if($entity.isRoot())
    $output.dynamicAnnotation("com.fasterxml.jackson.annotation.JsonIgnore")##
    public boolean isIdSet() {
#if ($entity.primaryKey.isComposite())
        return $entity.primaryKey.var != null && ${entity.primaryKey.var}.areFieldsSet();
#else
        return $entity.primaryKey.attribute.var != null#if ($entity.primaryKey.attribute.isString() && !$entity.primaryKey.attribute.isEnum()) && !${entity.primaryKey.attribute.var}.isEmpty()#end;
#end
    }
#end
}