module rainfall {
  exports rainfall.utility;
  exports rainfall.physical;
  exports rainfall.launcher;

  requires org.junit.jupiter.api;
  opens rainfall.qa.utility to org.junit.platform.commons;
  opens rainfall.qa.physical to org.junit.platform.commons;
  opens rainfall.qa.cli to org.junit.platform.commons;
  opens rainfall.qa.launcher to org.junit.platform.commons;
}
