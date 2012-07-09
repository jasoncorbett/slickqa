package org.tcrun.slickij.wro4j;

import org.apache.commons.io.FilenameUtils;
import ro.isdc.wro.extensions.processor.js.HandlebarsJsProcessor;
import ro.isdc.wro.extensions.processor.support.template.AbstractJsTemplateCompiler;
import ro.isdc.wro.model.resource.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: jcorbett
 * Date: 7/6/12
 * Time: 6:54 PM
 */
public class NamedHandlebarsProcessor extends HandlebarsJsProcessor
{
    public static final String ALIAS = "namedHandlebarsJs";

    @Override
    protected String getArgument(Resource resource) {
        final String name = resource == null ? "" : FilenameUtils.getBaseName(resource.getUri());
        return String.format("%s.html", name);
    }

    @Override
    protected AbstractJsTemplateCompiler createCompiler() {
        return new HandlebarsJsCompiler();
    }

}
