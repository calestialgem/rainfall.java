package rainfall.workspace;

import java.util.Map;

/**
 * Represents a Rainfall workspace.
 *
 * @param  <Model>  Type of the model compiler constructed for the sources in
 *                    the workspace.
 * @param  packages Packages in the workspace.
 * @author          calestialgem
 */
public record Workspace<Model>(Map<PhysicalName, Package<Model>> packages) {}
