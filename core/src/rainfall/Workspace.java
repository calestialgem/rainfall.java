package rainfall;

import java.util.Map;

record Workspace<Model>(Map<String, Package<Model>> packages) {}
