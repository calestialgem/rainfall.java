package rainfall.workspace;

public sealed interface Package<Model> {
  record File<Model>(Source<Model> contents) implements Package<Model> {}
  record Directory<Model>(Module<Model> contents) implements Package<Model> {}
}
