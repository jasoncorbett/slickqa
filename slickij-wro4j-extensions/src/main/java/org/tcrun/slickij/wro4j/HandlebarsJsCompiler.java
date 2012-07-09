package org.tcrun.slickij.wro4j;

import ro.isdc.wro.extensions.processor.support.handlebarsjs.HandlebarsJs;
import ro.isdc.wro.extensions.processor.support.template.AbstractJsTemplateCompiler;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 7/7/12
 * Time: 12:41 AM
 */
public class HandlebarsJsCompiler extends AbstractJsTemplateCompiler
{

  /**
   * visible for testing, the init of a HandlebarsJs template
   */
  public static final String HANDLEBARS_JS_TEMPLATES_INIT = "(function() { var template = Handlebars.template, "
      + "templates = Handlebars.templates = Handlebars.templates || {};";

  private static final String DEFAULT_HANDLEBARS_JS = "handlebars-1.0.0.beta.6.js";

  @Override
  public String compile(final String content, final String name) {

    return HANDLEBARS_JS_TEMPLATES_INIT + "templates['" + name + "'] = template("
        + super.compile(content, null) + " ); })();";
  }

  @Override
  protected String getCompileCommand() {
    return "Handlebars.precompile";
  }

  @Override
  protected InputStream getCompilerAsStream() {
    return HandlebarsJs.class.getResourceAsStream(DEFAULT_HANDLEBARS_JS);
  }

}
