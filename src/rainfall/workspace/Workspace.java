package rainfall.workspace;

import java.util.Map;

public record Workspace<Model>(Map<PhysicalName, Package<Model>> packages) {}
