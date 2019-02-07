$output.java($entity.repository)##

#if ($entity.hasUniqueBigIntegerAttribute())
$output.require("java.math.BigInteger")##
#end
#if ($entity.hasUniqueDateAttribute() || $entity.root.hasDatePk())
$output.require("java.util.Date")##
#end
$output.require("org.springframework.data.jpa.repository.*")##
$output.require("org.springframework.data.repository.CrudRepository")##
$output.require($entity.model)##
$output.require($entity.root.primaryKey)##
#foreach ($enumAttribute in $entity.uniqueEnumAttributes.list)
$output.require($enumAttribute)##
#end

/**
 * {@link GenericRepository} for {@link $entity.model.type} 
 */
$output.dynamicAnnotationTakeOver("org.springframework.stereotype.Repository")##
public ${output.abstractSpace} interface $output.currentClass extends CrudRepository<$entity.model.type, $entity.root.primaryKey.type> {

    
}