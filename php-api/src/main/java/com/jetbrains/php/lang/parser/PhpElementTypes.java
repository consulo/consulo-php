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

package com.jetbrains.php.lang.parser;

import com.jetbrains.php.lang.psi.elements.PhpFieldType;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.php.lang.psi.PhpElementType;

/**
 * Element types of the PHP tree, in PhpStorm's naming where it differs.
 * Stub-based declarations (class, function, method, field, parameter, namespace, define) are in the implementation.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public interface PhpElementTypes
{
	IElementType EMPTY_INPUT = new PhpElementType("Unrecognised input");
	IElementType HTML = new PhpElementType("HTML");
	IElementType GROUP_STATEMENT = new PhpElementType("GROUP_STATEMENT");
	IElementType STATEMENT = new PhpElementType("STATEMENT");
	IElementType MODIFIER_LIST = new PhpElementType("MODIFIER_LIST");
	IElementType USE_LIST = new PhpElementType("USE_LIST_STATEMENT");
	IElementType USE = new PhpElementType("USE");
	IElementType RETURN_TYPE = new PhpElementType("RETURN_TYPE");
	/**
	 * Type of a parameter, class constant or enum: {@link com.jetbrains.php.lang.psi.elements.PhpTypeDeclaration}
	 */
	IElementType TYPE_DECLARATION = new PhpElementType("TYPE_DECLARATION");
	/**
	 * Type of a property list: {@link PhpFieldType}
	 */
	IElementType FIELD_TYPE = new PhpElementType("FIELD_TYPE");
	IElementType ANONYMOUS_CLASS = new PhpElementType("ANONYMOUS_CLASS");
	IElementType EXPRESSION_PARAMETER_LIST = new PhpElementType("EXPRESSION_PARAMETER_LIST");
	IElementType UNARY_EXPRESSION = new PhpElementType("UNARY_EXPRESSION");
	IElementType ASSIGNMENT_EXPRESSION = new PhpElementType("ASSIGNMENT_EXPRESSION");
	IElementType SELF_ASSIGNMENT_EXPRESSION = new PhpElementType("SELF_ASSIGNMENT_EXPRESSION");
	IElementType TERNARY_EXPRESSION = new PhpElementType("TERNARY_EXPRESSION");
	IElementType ELVIS_EXPRESSION = new PhpElementType("ELVIS_EXPRESSION");
	IElementType INSTANCEOF_EXPRESSION = new PhpElementType("Instanceof expression");
	IElementType PRINT_EXPRESSION = new PhpElementType("Print expression");
	IElementType EXIT_EXPRESSION = new PhpElementType("Exit expression");
	IElementType ISSET_EXPRESSION = new PhpElementType("Isset function");
	IElementType NEW_EXPRESSION = new PhpElementType("New expression");
	IElementType INCLUDE_EXPRESSION = new PhpElementType("Include expression");
	IElementType EMPTY_EXPRESSION = new PhpElementType("Empty expression");
	IElementType EVAL_EXPRESSION = new PhpElementType("Eval expression");
	IElementType CLONE_EXPRESSION = new PhpElementType("Clone expression");
	IElementType MULTIASSIGNMENT_EXPRESSION = new PhpElementType("Multiassignment expression");
	IElementType LITERAL_LOGICAL_EXPRESSION = new PhpElementType("Literal logical expression");
	IElementType LOGICAL_EXPRESSION = new PhpElementType("Logical expression");
	IElementType BIT_EXPRESSION = new PhpElementType("Bint expression");
	IElementType EQUALITY_EXPRESSION = new PhpElementType("Equality expression");
	IElementType RELATIONAL_EXPRESSION = new PhpElementType("Relational expression");
	IElementType SHIFT_EXPRESSION = new PhpElementType("Shift expression");
	IElementType ADDITIVE_EXPRESSION = new PhpElementType("Additive expression");
	IElementType MULTIPLICATIVE_EXPRESSION = new PhpElementType("Multiplicative expression");
	IElementType TYPE_CAST_EXPRESSION = new PhpElementType("TYPE_CAST_EXPRESSION");
	IElementType SILENCE_EXPRESSION = new PhpElementType("Silence expression");
	IElementType POSTFIX_EXPRESSION = new PhpElementType("Postfix expression");
	IElementType COALESCE_EXPRESSION = new PhpElementType("COALESCE_EXPRESSION");
	IElementType CONCATENATION_EXPRESSION = new PhpElementType("CONCATENATION_EXPRESSION");
	IElementType PIPE_EXPRESSION = new PhpElementType("PIPE_EXPRESSION");
	IElementType POW_EXPRESSION = new PhpElementType("POW_EXPRESSION");
	IElementType PARENTHESIZED_EXPRESSION = new PhpElementType("PARENTHESIZED_EXPRESSION");
	IElementType MATCH_EXPRESSION = new PhpElementType("MATCH_EXPRESSION");
	IElementType MATCH_ARM = new PhpElementType("MATCH_ARM");
	IElementType THROW_EXPRESSION = new PhpElementType("THROW_EXPRESSION");
	IElementType VARIADIC_ARGUMENT = new PhpElementType("VARIADIC_ARGUMENT");
	IElementType NAMED_ARGUMENT = new PhpElementType("NAMED_ARGUMENT");
	IElementType CALLABLE_PLACEHOLDER = new PhpElementType("CALLABLE_PLACEHOLDER");
	IElementType NAMESPACE_REFERENCE = new PhpElementType("NAMESPACE_REFERENCE");
	IElementType CLOSURE_USES = new PhpElementType("CLOSURE_USES");
	IElementType ATTRIBUTES_LIST = new PhpElementType("ATTRIBUTES_LIST");
	IElementType ATTRIBUTE = new PhpElementType("ATTRIBUTE");
	IElementType ENUM_CASE = new PhpElementType("ENUM_CASE");
	IElementType PROPERTY_HOOKS = new PhpElementType("PROPERTY_HOOKS");
	IElementType PROPERTY_HOOK = new PhpElementType("PROPERTY_HOOK");
	IElementType TRAIT_USE = new PhpElementType("TRAIT_USE");
	IElementType TRAIT_ADAPTATION = new PhpElementType("TRAIT_ADAPTATION");
	IElementType LABEL = new PhpElementType("LABEL");
	IElementType GOTO = new PhpElementType("GOTO");
	IElementType HALT_COMPILER = new PhpElementType("HALT_COMPILER");
	IElementType CONSTANT_DECLARATION = new PhpElementType("CONSTANT_DECLARATION");
	IElementType ARRAY_CREATION_EXPRESSION = new PhpElementType("ARRAY_CREATION_EXPRESSION");
	IElementType ARRAY_ACCESS_EXPRESSION = new PhpElementType("ARRAY_ACCESS_EXPRESSION");
	IElementType ARRAY_HASH = new PhpElementType("ARRAY_HASH");
	IElementType YIELD = new PhpElementType("YIELD");
	TokenSet BINARY_EXPRESSIONS = TokenSet.create(ADDITIVE_EXPRESSION, MULTIPLICATIVE_EXPRESSION, SHIFT_EXPRESSION, RELATIONAL_EXPRESSION, EQUALITY_EXPRESSION, BIT_EXPRESSION, LOGICAL_EXPRESSION,
		LITERAL_LOGICAL_EXPRESSION, INSTANCEOF_EXPRESSION, COALESCE_EXPRESSION, CONCATENATION_EXPRESSION, PIPE_EXPRESSION, POW_EXPRESSION);
	IElementType IF = new PhpElementType("If");
	IElementType STRING = new PhpElementType("STRING_EXPRESSION");
	IElementType CONSTANT = new PhpElementType("Constant");
	IElementType CLASS_REFERENCE = new PhpElementType("CLASS_REFERENCE");
	IElementType VARIABLE_REFERENCE = new PhpElementType("VARIABLE_REFERENCE");
	IElementType ARRAY_INDEX = new PhpElementType("Array index");
	IElementType CLASS_CONSTANT_REFERENCE = new PhpElementType("CLASS_CONSTANT_REFERENCE");
	IElementType FINALLY_STATEMENT = new PhpElementType("FINALLY_STATEMENT");
	IElementType ELSE_IF = new PhpElementType("Elseif");
	IElementType ELSE = new PhpElementType("Else");
	IElementType ARRAY = new PhpElementType("Array");
	IElementType ARRAY_KEY = new PhpElementType("Array key");
	IElementType ARRAY_VALUE = new PhpElementType("Array value");
	IElementType FOR = new PhpElementType("For");
	IElementType FOREACH = new PhpElementType("Foreach");
	IElementType WHILE = new PhpElementType("While");
	IElementType DO_WHILE = new PhpElementType("Do while");
	IElementType BREAK = new PhpElementType("Break");
	IElementType CONTINUE = new PhpElementType("Continue");
	IElementType ECHO = new PhpElementType("Echo");
	IElementType GLOBAL = new PhpElementType("Global");
	IElementType UNSET = new PhpElementType("Unset");
	IElementType IS_REFERENCE = new PhpElementType("Is reference");
	IElementType PARAMETER_DEFAULT_VALUE = new PhpElementType("PhpParameter default value");
	IElementType EXTENDS_LIST = new PhpElementType("Extends list");
	IElementType IMPLEMENTS_LIST = new PhpElementType("Implements list");
	IElementType ANONYMOUS_FUNCTION = new PhpElementType("PHP_ANONYMOUS_FUNCTION");
	IElementType SWITCH = new PhpElementType("Switch statement");
	IElementType CASE_DEFAULT = new PhpElementType("Default case");
	IElementType CASE = new PhpElementType("Case");
	IElementType RETURN = new PhpElementType("RETURN_STATEMENT");
	IElementType STATIC = new PhpElementType("Static statement");
	IElementType DECLARE = new PhpElementType("Declare statement");
	IElementType DECLARE_DIRECTIVE = new PhpElementType("Declare directive");
	IElementType TRY = new PhpElementType("Try statement");
	IElementType CATCH = new PhpElementType("Catch clause");
	IElementType THROW = new PhpElementType("Throw statement");
	IElementType EXPRESSION = new PhpElementType("Expression");
	IElementType OBSCURE_VARIABLE = new PhpElementType("Obscure variable");
	IElementType FUNCTION_REFERENCE = new PhpElementType("FUNCTION_REFERENCE");
	IElementType FIELD_REFERENCE = new PhpElementType("PhpField reference");
	IElementType METHOD_REFERENCE = new PhpElementType("METHOD_REFERENCE");
	IElementType HEREDOC = new PhpElementType("Heredoc");
	IElementType VARIABLE_NAME = new PhpElementType("PhpVariableReference name");
	IElementType NUMBER = new PhpElementType("Number");
	IElementType SHELL_COMMAND = new PhpElementType("Shell command");
	IElementType PHP_OUTER_TYPE = new PhpElementType("PHP outer type in html");

	// PhpStorm names
	IElementType PARAMETER_LIST = EXPRESSION_PARAMETER_LIST;
	/**
	 * The expression of a closure, its child is the {@link #ANONYMOUS_FUNCTION}
	 */
	IElementType CLOSURE = new PhpElementType("CLOSURE");
	IElementType NON_LAZY_GROUP_STATEMENT = GROUP_STATEMENT;
	IElementType HASH_ARRAY_ELEMENT = ARRAY_HASH;
	IElementType PHP_ARRAY_KEY = ARRAY_KEY;
	IElementType PHP_ARRAY_VALUE = ARRAY_VALUE;
	/**
	 * {@code [attributes] modifiers [type] $a = 1, $b;} - one {@code CLASS_FIELD} per variable
	 */
	IElementType CLASS_FIELDS = new PhpElementType("CLASS_FIELDS");
	/**
	 * {@code [attributes] modifiers const [type] A = 1, B = 2;} - one {@code CLASS_CONSTANT} per name
	 */
	IElementType CLASS_CONSTANTS = new PhpElementType("CLASS_CONSTANTS");
}
