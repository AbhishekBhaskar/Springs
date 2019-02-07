$output.java($entity.dtoservice)##

$output.require("javax.inject.Inject")##
$output.require("java.util.List")##
$output.require("java.util.ArrayList")##
$output.require("java.util.stream.Collectors")##
$output.require($entity.root.primaryKey)##
$output.require("java.lang.Iterable")##
$output.require($entity.model)##
$output.requireMetamodel($entity.model)##
$output.require("org.springframework.transaction.annotation.Transactional")##
$output.require("org.springframework.stereotype.Service") ##
$output.require("org.springframework.data.domain.Example")##
$output.require("org.springframework.data.domain.ExampleMatcher")##
$output.require("org.springframework.data.domain.Page")##
$output.require($entity.repository)##

/**
 * A simple DTO Facility for ${entity.model.type}.
 */
@Service
public class $output.currentClass {

    @Inject
    private $entity.repository.type $entity.repository.var;
#foreach ($relation in $entity.xToOne.list)
#if(!$relation.to.type.equals($entity.model.type))
#if($output.requireFirstTime($relation.toEntity.dtoservice))
    @Inject
    private $relation.toEntity.dtoservice.type $relation.toEntity.dtoservice.var;
#end
#if($output.requireFirstTime($relation.toEntity.repository))
$output.require($relation.toEntity.model) ##
    @Inject
    private $relation.toEntity.repository.type $relation.toEntity.repository.var;
#end
#end
#end
#foreach ($relation in $entity.manyToMany.list)
#if(!$relation.to.type.equals($entity.model.type))
#if($output.requireFirstTime($relation.toEntity.dtoservice))
    @Inject
    private $relation.toEntity.dtoservice.type $relation.toEntity.dtoservice.var;
#end
#if($output.requireFirstTime($relation.toEntity.repository))
$output.require($relation.toEntity.model) ##
    @Inject
    private $relation.toEntity.repository.type $relation.toEntity.repository.var;
#end
#end
#end

    @Transactional(readOnly = true)
    public $entity.dto.type findOne($entity.root.primaryKey.type $entity.root.primaryKey.var) {
        return toDTO(${entity.repository.var}.findById($entity.root.primaryKey.var).get());
    }

    /*@Transactional(readOnly = true)
    public List<$entity.dto.type> complete(String query, int maxResults) {
        List<$entity.model.type> results = ${entity.repository.var}.complete(query, maxResults);
        return results.stream().map(this::toDTO).collect(Collectors.toList());
    }*/

    @Transactional(readOnly = true)
    public List<$entity.model.type> findAll() {
    	
    	Iterable<$entity.model.type>  page = ${entity.repository.var}.findAll();
    	List<$entity.model.type> content = new ArrayList<$entity.model.type>();
        page.forEach(content::add);
        
        
        return content;
    }

    /**
     * Save the passed dto as a new entity or update the corresponding entity if any.
     */
    @Transactional
    public $entity.dto.type save($entity.dto.type dto) {
        if (dto == null) {
            return null;
        }

        final $entity.model.type $entity.model.var;

        if (dto.isIdSet()) {
            $entity.model.type ${entity.model.var}Tmp = ${entity.repository.var}.findById(dto.$identifiableProperty.var).get();
            if (${entity.model.var}Tmp != null) {
                $entity.model.var = ${entity.model.var}Tmp;
            } else {
                $entity.model.var = new ${entity.model.type}();
                ${entity.model.var}.${identifiableProperty.setter}(dto.$identifiableProperty.var);
            }
        } else {
            $entity.model.var = new ${entity.model.type}();
        }
#foreach ($attribute in $entity.nonCpkAttributes.list)
#if(!$attribute.isInFk() && !$attribute.isSimplePk() && !($attribute.isInFileDefinition() || $attribute.isBinary()))

        ${entity.model.var}.${attribute.setter}(dto.$attribute.var);
#end
#end
#foreach ($relation in $entity.forwardXToOne.list)

        if (dto.$relation.to.var == null) {
            ${entity.model.var}.${relation.to.setter}(null);
        } else {
            $relation.to.type $relation.to.var = ${entity.model.var}.${relation.to.getter}();
            if ($relation.to.var == null || (${relation.to.var}.getId().compareTo(dto.${relation.to.var}.id) != 0)) {
                ${entity.model.var}.${relation.to.setter}(${relation.toEntity.repository.var}.findById(dto.${relation.to.var}.id).get());
            }
        }
#end
#foreach ($relation in $entity.manyToMany.list)

        ${entity.model.var}.${relation.to.getters}().clear();
        if (dto.$relation.to.vars != null) {
            dto.${relation.to.vars}.stream().forEach(
                $relation.to.var -> ${entity.model.var}.${relation.to.adder}(${relation.toEntity.repository.var}.findById(${relation.to.var}.id).get()));
        }
#end

        return toDTO(${entity.repository.var}.save($entity.model.var));
    }

    /**
     * Converts the passed $entity.model.var to a DTO.
     */
    public $entity.dto.type toDTO($entity.model.type $entity.model.var){
        return toDTO($entity.model.var, 1);
    }

    /**
     * Converts the passed $entity.model.var to a DTO. The depth is used to control the
     * amount of association you want. It also prevents potential infinite serialization cycles.
     *
     * @param $entity.model.var
     * @param depth the depth of the serialization. A depth equals to 0, means no x-to-one association will be serialized.
     *              A depth equals to 1 means that xToOne associations will be serialized. 2 means, xToOne associations of
     *              xToOne associations will be serialized, etc.
     */
    public $entity.dto.type toDTO($entity.model.type $entity.model.var, int depth) {
        if ($entity.model.var == null) {
            return null;
        }

        $entity.dto.type dto = new ${entity.dto.type}();

#if ($entity.isRoot() && $entity.primaryKey.isComposite())
$output.require($entity.primaryKey)##
        dto.$entity.primaryKey.var = ${entity.model.var}.${entity.primaryKey.getter}();
#end
#foreach ($attribute in $entity.nonCpkAttributes.list)
#if(!$attribute.isInFk() && !$attribute.isBinary())
        dto.$attribute.var = ${entity.model.var}.${attribute.getter}();
#end
#end
        if (depth-- > 0) {
#foreach ($relation in $entity.xToOne.list)
#if($relation.to.type.equals($entity.model.type))
            dto.$relation.to.var = toDTO(${entity.model.var}.${relation.to.getter}(), depth);
#else
            dto.$relation.to.var = ${relation.toEntity.dtoservice.var}.toDTO(${entity.model.var}.${relation.to.getter}(), depth);
#end
#end
#foreach ($relation in $entity.manyToMany.list)
#if($velocityCount == 1)
            final int fdepth = depth;
#end
#if($relation.to.type.equals($entity.model.type))
            dto.$relation.to.vars = ${entity.model.var}.${relation.to.getters}().stream().
                map($relation.to.var -> toDTO($relation.to.var, fdepth)).collect(Collectors.toList());
#else
            dto.$relation.to.vars = ${entity.model.var}.${relation.to.getters}().stream().
                map($relation.to.var -> ${relation.toEntity.dtoservice.var}.toDTO($relation.to.var, fdepth)).collect(Collectors.toList());
#end
#end
        }

        return dto;
    }

    /**
     * Converts the passed dto to a ${entity.model.type}.
     * Convenient for query by example.
     */
    public $entity.model.type toEntity($entity.dto.type dto){
        return toEntity(dto, 1);
    }

    /**
     * Converts the passed dto to a ${entity.model.type}.
     * Convenient for query by example.
     */
    public $entity.model.type toEntity($entity.dto.type dto, int depth) {
        if (dto == null) {
            return null;
        }

        $entity.model.type $entity.model.var = new ${entity.model.type}();

#if ($entity.isRoot() && $entity.primaryKey.isComposite())
        ${entity.model.var}.${entity.primaryKey.setter}(dto.id);
#end
#foreach ($attribute in $entity.nonCpkAttributes.list)
#if(!$attribute.isInFk() && !($attribute.isInFileDefinition() || $attribute.isBinary()))
        ${entity.model.var}.${attribute.setter}(dto.$attribute.var);
#end
#end
        if (depth-- > 0) {
#foreach ($relation in $entity.xToOne.list)
#if($relation.to.type.equals($entity.model.type))
            ${entity.model.var}.${relation.to.setter}(toEntity(dto.${relation.to.var}, depth));
#else
            ${entity.model.var}.${relation.to.setter}(${relation.toEntity.dtoservice.var}.toEntity(dto.${relation.to.var}, depth));
#end
#end
        }

        return $entity.model.var;
    }
}