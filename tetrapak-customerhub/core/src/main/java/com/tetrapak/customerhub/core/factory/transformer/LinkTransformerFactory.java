package com.tetrapak.customerhub.core.factory.transformer;

import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.customerhub.core.transformer.impl.CustomLinkTransformer;

@Component(property = { "pipeline.type=cuhu-link-rewrite" /*"pipeline.mode=global", "service.ranking:Integer=1001" */}, service = { TransformerFactory.class })
public class LinkTransformerFactory implements TransformerFactory {

	private static final Logger log = LoggerFactory.getLogger(LinkTransformerFactory.class);
	
	@Override
	public Transformer createTransformer() {
		log.info("createTransformer");
		return new CustomLinkTransformer();
	}

}
