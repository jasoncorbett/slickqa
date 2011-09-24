package org.tcrun.slickij.api.data;

import java.io.Serializable;

/**
 *
 * @author jcorbett
 */
public enum ResultStatus implements Serializable
{
	PASS,
	FAIL,
	NOT_TESTED,
	NO_RESULT,
	BROKEN_TEST,
	SKIPPED,
    CANCELLED
}
