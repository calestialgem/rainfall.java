package rainfall;

import java.util.Map;

record Workspace<Model>(Map<UTF8, Package<Model>> packages) {}
