package com.tetrapak.customerhub.core.factory.transformer;

import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.customerhub.core.transformer.impl.CustomLinkTransformer;

/**
 * 
 * This class is used to create a CustomLinkTransformer object from
 * LinkTransformerFactory having pipeline type as cuhu-link-rewrite
 * 
 * @author Swati Lamba
 *
 */
@Component(property = { "pipeline.type=cuhu-link-rewrite" }, service = { TransformerFactory.class })
public class LinkTransformerFactory implements TransformerFactory {

	private static final Logger LOG = LoggerFactory.getLogger(LinkTransformerFactory.class);

	@Override
	public Transformer createTransformer() {
		LOG.debug("CustomLinkTransformer object created");
		return new CustomLinkTransformer();
	}

}
