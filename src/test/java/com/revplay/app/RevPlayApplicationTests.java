package com.revplay.app;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("RevPlay Full Application Test Suite")
@SelectPackages("com.revplay.app")
public class RevPlayApplicationTests {
}
