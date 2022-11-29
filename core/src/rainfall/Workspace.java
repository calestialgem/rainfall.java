package rainfall;

import java.util.Map;

record Workspace<Model>(Map<Unicode, Package<Model>> packages) {}
