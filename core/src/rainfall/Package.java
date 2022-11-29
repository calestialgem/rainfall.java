package rainfall;

sealed interface Package<Model> {
  record Directory<Model>(Module<Model> module) implements Package<Model> {}
  record File<Model>(Source<Model> source) implements Package<Model> {}
}
