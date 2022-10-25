package rainfall.workspace;

/**
 * Represents a Thrice package. Thrice sources are modeled with the given extra
 * data.
 */
public sealed abstract class Package<Model> permits FilePackage<Model>, DirectoryPackage<Model> {
}
