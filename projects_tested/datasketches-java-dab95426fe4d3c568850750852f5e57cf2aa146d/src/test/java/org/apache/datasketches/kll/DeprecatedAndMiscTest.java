/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.datasketches.kll;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * @author Lee Rhodes
 */
@SuppressWarnings("javadoc")
public class DeprecatedAndMiscTest {

  @SuppressWarnings("deprecation")
  @Test
  public void checkDeprecatedRankError() {
    final KllFloatsSketch sketch = new KllFloatsSketch();
    final int k = KllFloatsSketch.DEFAULT_K;
    final double eps1 = sketch.getNormalizedRankError(); //v2.0.0.
    final double eps2 = KllFloatsSketch.getNormalizedRankError(k); //v2.0.0.
    final double expectEps = KllFloatsSketch.getNormalizedRankError(k, true);
    assertEquals(eps1, expectEps);
    assertEquals(eps2, expectEps);
  }

  @Test
  public void checkGetKFromEps() {
    final int k = KllFloatsSketch.DEFAULT_K;
    final double eps = KllFloatsSketch.getNormalizedRankError(k, false);
    final double epsPmf = KllFloatsSketch.getNormalizedRankError(k, true);
    final int kEps = KllFloatsSketch.getKFromEpsilon(eps, false);
    final int kEpsPmf = KllFloatsSketch.getKFromEpsilon(epsPmf, true);
    assertEquals(kEps, k);
    assertEquals(kEpsPmf, k);
  }

  @Test
  public void checkBounds() {
    final KllFloatsSketch kll = new KllFloatsSketch(); //default k = 200
    for (int i = 0; i < 1000; i++) {
      kll.update(i);
    }
    final double eps = kll.getNormalizedRankError(false);
    final float est = kll.getQuantile(0.5);
    final float ub = kll.getQuantileUpperBound(0.5);
    final float lb = kll.getQuantileLowerBound(0.5);
    assertEquals(ub, kll.getQuantile(.5 + eps));
    assertEquals(lb, kll.getQuantile(0.5 - eps));
    println("Ext     : " + est);
    println("UB      : " + ub);
    println("LB      : " + lb);
  }

  //@Test //requires visual check
  public void checkNumRetainedAboveLevelZero() {
    final KllFloatsSketch sketch = new KllFloatsSketch(20);
    for (int i = 0; i < 10; i++) { sketch.update(i + 1); }
    final String s1 = sketch.toString(true, true);
    println(s1);
    final KllFloatsSketch sketch2 = new KllFloatsSketch(20);
    for (int i = 0; i < 400; i++) {
      sketch2.update(i + 1);
    }
    sketch2.merge(sketch);
    final String s2 = sketch2.toString(true, true);
    println(s2);
  }

  @Test
  public void printlnTest() {
    println("PRINTING: " + this.getClass().getName());
  }

  /**
   * @param s value to print
   */
  static void println(final String s) {
    //System.out.println(s); //disable here
  }

}
