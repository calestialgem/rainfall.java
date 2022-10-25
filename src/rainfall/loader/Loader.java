package rainfall.loader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import rainfall.utility.Result;
import rainfall.workspace.DirectoryPackage;
import rainfall.workspace.FilePackage;
import rainfall.workspace.Package;
import rainfall.workspace.PhysicalName;
import rainfall.workspace.Source;
import rainfall.workspace.Module;
import rainfall.workspace.Workspace;

/**
 * Loads the packages in the workspace.
 */
public final class Loader {
  /**
   * Path to the directory of the workspace that is loaded.
   */
  public final Path loaded;

  /**
   * Packages that are loaded.
   */
  private Map<PhysicalName, Package<Linear>> packages;

  /**
   * Constructs a loader.
   *
   * @param loaded Path to the workspace directory that is loaded by the
   *               constructed loader.
   */
  public Loader(Path loaded) {
    this.loaded = loaded;
  }

  /**
   * Loads the workspace.
   *
   * @return Linearly modelled workspace.
   */
  public Workspace<Linear> load() {
    // Initialize mutable state.
    packages = new HashMap<>();

    // Get all the files in the workspace directory.
    var files = loaded.toFile().listFiles();

    // Try loading all the files that have the Thrice source file extension.
    Stream.of(files).filter(File::isFile)
      .map(File::toPath)
      .forEach(this::loadFilePackage);

    // Try loading all the directories.
    Stream.of(files).filter(File::isDirectory)
      .map(File::toPath)
      .forEach(this::loadDirectoryPackage);

    return new Workspace<>(loaded, packages);
  }

  /**
   * Tries to load a file package at the given file.
   *
   * @param file File that is loaded, which has a valid physical name.
   */
  private void loadFilePackage(Path file) {
    var source = loadSource(file);
    if (source.isFailed())
      System.err.printf("Failed to load the package at `%s` due to:%n%s",
        file.toString(), source.getError());
    else if (source.getValue().isPresent())
      packages.put(source.getValue().get().name,
        new FilePackage<>(source.getValue().get()));
  }

  /**
   * Tries to load a directory package at the given directory.
   *
   * @param directory Directory that is loaded, which has a valid physical name.
   */
  private void loadDirectoryPackage(Path directory) {
    var module = loadModule(directory);
    if (module.isFailed())
      System.err.printf("Failed to load the package at `%s` due to:%n%s",
        directory.toString(), module.getError());
    else if (module.getValue().isPresent())
      packages.put(module.getValue().get().name,
        new DirectoryPackage<>(module.getValue().get()));
  }

  /**
   * Tries to load a file as a source.
   *
   * @param file File that is loaded.
   *
   * @return Loaded source.
   */
  private static Result<Optional<Source<Linear>>, String>
    loadSource(Path file) {
    // Check file extension.
    var fileName = file.getFileName().toString();
    if (!fileName.endsWith(".tr"))
      return Result.ofSuccess(Optional.empty());

    // Check file name; if the extension is there but the file name does not
    // match, that is an error.
    var fileStub   = fileName.substring(0, fileName.length() - ".tr".length());
    var nameResult = PhysicalName.of(fileStub);
    if (nameResult.isFailed())
      return Result.ofFailure("Failed to load the source at `%s` due to:%n%s"
        .formatted(file.toString(), nameResult.getError()));

    // Load the contents.
    try {
      var contents = Files.readString(file).toCharArray();
      var model    = new Linear(contents);
      var source   = new Source<>(nameResult.getValue(), file, model);
      return Result.ofSuccess(Optional.of(source));
    } catch (IOException exception) {
      return Result.ofFailure("Failed to load the source at `%s` due to:%n%s"
        .formatted(file.toString(), exception.getLocalizedMessage()));
    }
  }

  /**
   * Tries to load a directory as a module.
   *
   * @param directory Directory that is loaded.
   *
   * @return Loaded module.
   */
  private static Result<Optional<Module<Linear>>, String>
    loadModule(Path directory) {
    // Check the directory name: compared to files, if the name does not match
    // it is not an error. Because, the files have an extension, which indicates
    // they are a Thrice source.
    var fileName   = directory.getFileName().toString();
    var nameResult = PhysicalName.of(fileName);
    if (nameResult.isFailed())
      return Result.ofSuccess(Optional.empty());

    // Accumulate results and errors.
    var sources    = new HashMap<PhysicalName, Source<Linear>>();
    var submodules = new HashMap<PhysicalName, Module<Linear>>();
    var errors     = new ArrayList<String>();

    // For every entry in the directory, recursively load directories as modules
    // and load files as sources.
    for (File entry : directory.toFile().listFiles()) {
      if (entry.isDirectory()) {
        var module = loadModule(entry.toPath());
        if (module.isFailed())
          errors.add(module.getError());
        else if (module.getValue().isPresent())
          submodules.put(module.getValue().get().name, module.getValue().get());
      } else if (entry.isFile()) {
        var source = loadSource(entry.toPath());
        if (source.isFailed())
          errors.add(source.getError());
        else if (source.getValue().isPresent())
          sources.put(source.getValue().get().name, source.getValue().get());
      }
    }

    // If there were an error, join all of them and return.
    if (!errors.isEmpty())
      return Result.ofFailure("Failed to load the module at `%s` due to:%n%s"
        .formatted(directory.toString(),
          String.join(System.lineSeparator(), errors.toArray(String[]::new))));

    // Return the module if it is not empty. If there are no source files, this
    // is a random directory whose name happened to match the rules of a Thrice
    // module.
    var module = new Module<>(nameResult.getValue(), sources, submodules);
    return Result
      .ofSuccess(module.isEmpty() ? Optional.empty() : Optional.of(module));
  }
}
