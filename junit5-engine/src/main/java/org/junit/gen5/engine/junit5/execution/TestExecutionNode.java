/*
 * Copyright 2015 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.junit.gen5.engine.junit5.execution;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.gen5.api.extension.TestExecutionContext;
import org.junit.gen5.engine.ExecutionRequest;
import org.junit.gen5.engine.TestDescriptor;

/**
 * @author Stefan Bechtold
 * @author Sam Brannen
 * @since 5.0
 */
public abstract class TestExecutionNode {

	private TestExecutionNode parent;

	private List<TestExecutionNode> children = new LinkedList<>();

	public void addChild(TestExecutionNode child) {
		this.children.add(child);
		child.parent = this;
	}

	public final TestExecutionNode getParent() {
		return this.parent;
	}

	public final List<TestExecutionNode> getChildren() {
		return Collections.unmodifiableList(this.children);
	}

	public abstract TestDescriptor getTestDescriptor();

	public abstract void execute(ExecutionRequest request, TestExecutionContext context);

	protected void executeChild(TestExecutionNode child, ExecutionRequest request, TestExecutionContext parentContext,
			Object testInstance) {
		TestExecutionContext childContext = createChildContext(child, parentContext, testInstance);
		child.execute(request, childContext);
	}

	private TestExecutionContext createChildContext(TestExecutionNode child, TestExecutionContext parentContext,
			Object testInstance) {
		return new DescriptorBasedTestExecutionContext(child.getTestDescriptor(), parentContext, testInstance);
	}

}
