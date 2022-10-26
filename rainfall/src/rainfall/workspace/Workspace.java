package rainfall.workspace;

import java.util.Map;

public record Workspace<Model> (Map<Designation, Package<Model>> packages) {}
