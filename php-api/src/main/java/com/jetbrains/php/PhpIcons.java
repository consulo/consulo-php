/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jetbrains.php;

import consulo.php.icon.PhpIconGroup;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.ui.image.Image;

/**
 * PHP icons under PhpStorm's names, backed by the platform and {@link PhpIconGroup} icons.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public interface PhpIcons
{
	Image PhpIcon = PhpIconGroup.php();
	Image PHP_FILE = PhpIconGroup.filetypesPhp();

	Image NAMESPACE = PlatformIconGroup.nodesNamespace();
	Image CLASS = PlatformIconGroup.nodesClass();
	Image ABSTRACT_CLASS = PlatformIconGroup.nodesAbstractclass();
	Image EXCEPTION = PlatformIconGroup.nodesExceptionclass();
	Image INTERFACE = PlatformIconGroup.nodesInterface();
	Image TRAIT = PlatformIconGroup.nodesTrait();
	Image ENUM = PlatformIconGroup.nodesEnum();
	Image CLASS_ATTRIBUTE = PlatformIconGroup.nodesAttribute();

	Image METHOD = PlatformIconGroup.nodesMethod();
	Image ABSTRACT_METHOD = PlatformIconGroup.nodesAbstractmethod();
	Image CONSTRUCTOR = PlatformIconGroup.nodesMethod();
	Image FUNCTION = PlatformIconGroup.nodesFunction();
	Image CLOSURE = PlatformIconGroup.nodesLambda();

	Image FIELD = PlatformIconGroup.nodesField();
	Image STATIC_FIELD = PlatformIconGroup.nodesField();
	Image CONSTANT = PlatformIconGroup.nodesConstant();
	Image ENUM_CASE = PlatformIconGroup.nodesConstant();
	Image VARIABLE = PlatformIconGroup.nodesVariable();
	Image PARAMETER = PlatformIconGroup.nodesParameter();

	// gutter
	Image IMPLEMENTS = PlatformIconGroup.gutterImplementingmethod();
	Image IMPLEMENTED = PlatformIconGroup.gutterImplementedmethod();
	Image OVERRIDES = PlatformIconGroup.gutterOverridingmethod();
	Image OVERRIDEN = PlatformIconGroup.gutterOverridenmethod();
}
