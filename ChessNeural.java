package reinforcechess;

import java.util.Arrays;

public class ChessNeural {

	double[][] synapse0;
	double[][] synapse1;
	double[][] synapse2;
	double[][] synapse3;
	double[][] synapse4;
	double[][] synapse5;
	double[][] synapse6;
	
	double[][] finalSynapse;
	double[][] INPUT_VALUES;
	double[][] OUTPUT_VALUES;
	double correct;
	int nodesPerLayer;
	double learningSpeed;

	public ChessNeural(double[][] a, double[][] b, int d, double e) {
		INPUT_VALUES=a;
		OUTPUT_VALUES=b;
		nodesPerLayer=d;
		synapse0 = mxjava.synapseLayer(a[0].length,d);
		synapse1 = mxjava.synapseLayer(d,d);
		synapse2 = mxjava.synapseLayer(d,d);
		synapse3 = mxjava.synapseLayer(d,d);
		synapse4 = mxjava.synapseLayer(d,d);
		synapse5 = mxjava.synapseLayer(d,d);
		synapse6 = mxjava.synapseLayer(d,d);
		
		finalSynapse = mxjava.synapseLayer(nodesPerLayer,OUTPUT_VALUES[0].length);
		correct = 0;
		learningSpeed = e;
	}
	
	public void newSynapseWeights() {
		
		synapse0 = mxjava.synapseLayer(INPUT_VALUES[0].length,nodesPerLayer);
		synapse1 = mxjava.synapseLayer(nodesPerLayer,nodesPerLayer);
		synapse2 = mxjava.synapseLayer(nodesPerLayer,nodesPerLayer);
		synapse3 = mxjava.synapseLayer(nodesPerLayer,nodesPerLayer);
		synapse4 = mxjava.synapseLayer(nodesPerLayer,nodesPerLayer);
		synapse5 = mxjava.synapseLayer(nodesPerLayer,nodesPerLayer);
		synapse6 = mxjava.synapseLayer(nodesPerLayer,nodesPerLayer);
		finalSynapse = mxjava.synapseLayer(nodesPerLayer,OUTPUT_VALUES[0].length);
		
	}
	
	public void printSynapseLayer(double[][] synapses) {
		for (int i = 0;i<synapses.length;i++) {
			System.out.println(Arrays.toString(synapses[i]));
		}
	}
	
	public void printSynapses() {
		System.out.println("Synapse 0");
		printSynapseLayer(synapse0);
		System.out.println("Synapse 1");
		printSynapseLayer(synapse1);
		System.out.println("Synapse 2");
		printSynapseLayer(synapse2);
		System.out.println("Synapse 3");
		printSynapseLayer(synapse3);
		System.out.println("Synapse 4");
		printSynapseLayer(synapse4);
		System.out.println("Synapse 5");
		printSynapseLayer(synapse5);
		System.out.println("Synapse 6");
		printSynapseLayer(synapse6);	
	}
	

	public void trainNetwork(int c) {
		int iterations = c;
		//System.out.println("--BEGIN TRAINING--");
		for (int runs = 0;runs<=(iterations-1);runs++) {
			
			 
			double[][] layer0 = INPUT_VALUES;
			double[][] rawLayer1 = mxjava.matrixMult(layer0,synapse0);
			double[][] layer1 = new double[rawLayer1.length][rawLayer1[0].length];
			for (int i = 0;i<rawLayer1.length;i++) {
				for (int j = 0;j<rawLayer1[0].length;j++) {
					layer1[i][j] = mxjava.sigmoidPackage(rawLayer1[i][j],false);
				}
			} //should return a INPUT_VALUES.length x nodesPerLayer matrix

			double[][] rawLayer2 = mxjava.matrixMult(layer1,synapse1);
			double[][] layer2 = new double[rawLayer2.length][rawLayer2[0].length];
			for (int i = 0;i<rawLayer2.length;i++) {
				for (int j = 0;j<rawLayer2[0].length;j++) {
					layer2[i][j] = mxjava.sigmoidPackage(rawLayer2[i][j],false);
				}
			} //returns INPUT_VALUES.length x nodesPerLayer matrix
			
			double[][] rawLayer3 = mxjava.matrixMult(layer2,synapse2);
			double[][] layer3 = new double[rawLayer3.length][rawLayer3[0].length];
			for (int i = 0;i<rawLayer3.length;i++) {
				for (int j = 0;j<rawLayer3[0].length;j++) {
					layer3[i][j] = mxjava.sigmoidPackage(rawLayer3[i][j],false);
				}
			} //returns INPUT_VALUES.length x nodesPerLayer matrix
			
			double[][] rawLayer4 = mxjava.matrixMult(layer3,synapse3);
			double[][] layer4 = new double[rawLayer4.length][rawLayer4[0].length];
			for (int i = 0;i<rawLayer4.length;i++) {
				for (int j = 0;j<rawLayer4[0].length;j++) {
					layer4[i][j] = mxjava.sigmoidPackage(rawLayer4[i][j],false);
				}
			} //returns INPUT_VALUES.length x nodesPerLayer matrix
			
			double[][] rawLayer5 = mxjava.matrixMult(layer4,synapse4);
			double[][] layer5 = new double[rawLayer5.length][rawLayer5[0].length];
			for (int i = 0;i<rawLayer5.length;i++) {
				for (int j = 0;j<rawLayer5[0].length;j++) {
					layer5[i][j] = mxjava.sigmoidPackage(rawLayer5[i][j],false);
				}
			} //returns INPUT_VALUES.length x nodesPerLayer matrix
			
			double[][] rawLayer6 = mxjava.matrixMult(layer5,synapse5);
			double[][] layer6 = new double[rawLayer6.length][rawLayer6[0].length];
			for (int i = 0;i<rawLayer6.length;i++) {
				for (int j = 0;j<rawLayer6[0].length;j++) {
					layer6[i][j] = mxjava.sigmoidPackage(rawLayer6[i][j],false);
				}
			} //returns INPUT_VALUES.length x nodesPerLayer matrix
			
			double[][] rawLayer7 = mxjava.matrixMult(layer6,synapse6);
			double[][] layer7 = new double[rawLayer7.length][rawLayer7[0].length];
			for (int i = 0;i<rawLayer7.length;i++) {
				for (int j = 0;j<rawLayer7[0].length;j++) {
					layer7[i][j] = mxjava.sigmoidPackage(rawLayer7[i][j],false);
				}
			} //returns INPUT_VALUES.length x nodesPerLayer matrix


			double[][] rawFinalLayer = mxjava.matrixMult(layer7,finalSynapse);
			double[][] finalLayer = new double[rawFinalLayer.length][rawFinalLayer[0].length];
			for (int i = 0;i<rawFinalLayer.length;i++) {
				for (int j = 0;j<rawFinalLayer[0].length;j++) {
					finalLayer[i][j] = mxjava.sigmoidPackage(rawFinalLayer[i][j],false);
				}
			} //returns INPUT_VALUES.length x OUTPUT_VALUES[0].length matrix.

			//finalLayer delta
			double[][] finalLayerError = mxjava.subtract(OUTPUT_VALUES,finalLayer);
			double[][] sigmoidDerivativeForfinalLayer = new double[finalLayer.length][finalLayer[0].length];
			for (int i = 0;i<finalLayer.length;i++) {
				for (int j = 0;j<finalLayer[0].length;j++) {
					sigmoidDerivativeForfinalLayer[i][j] = mxjava.sigmoidPackage(finalLayer[i][j],true);
				}
			}
			double[][] finalLayerDelta = mxjava.scalarMult(finalLayerError,sigmoidDerivativeForfinalLayer);

			
			//layer 7 delta
			double[][] layer7Error = mxjava.matrixMult(finalLayerDelta,mxjava.transpose(finalSynapse));
			double[][] sigmoidDerivativeForLayer7 = new double[layer7.length][layer7[0].length];
			for (int i = 0;i<layer1.length;i++) {
				for (int j = 0;j<layer1[0].length;j++) {
					sigmoidDerivativeForLayer7[i][j] = mxjava.sigmoidPackage(layer7[i][j],true);
				}
			}
			double[][] layer7Delta = mxjava.scalarMult(layer7Error,sigmoidDerivativeForLayer7);
			
			
			//layer 6 delta
			double[][] layer6Error = mxjava.matrixMult(layer7Delta,mxjava.transpose(synapse6));
			double[][] sigmoidDerivativeForLayer6 = new double[layer6.length][layer6[0].length];
			for (int i = 0;i<layer1.length;i++) {
				for (int j = 0;j<layer1[0].length;j++) {
					sigmoidDerivativeForLayer6[i][j] = mxjava.sigmoidPackage(layer6[i][j],true);
				}
			}
			double[][] layer6Delta = mxjava.scalarMult(layer6Error,sigmoidDerivativeForLayer6);
			
			
			//layer 5 delta
			double[][] layer5Error = mxjava.matrixMult(layer6Delta,mxjava.transpose(synapse5));
			double[][] sigmoidDerivativeForLayer5 = new double[layer5.length][layer5[0].length];
			for (int i = 0;i<layer1.length;i++) {
				for (int j = 0;j<layer1[0].length;j++) {
					sigmoidDerivativeForLayer5[i][j] = mxjava.sigmoidPackage(layer5[i][j],true);
				}
			}
			double[][] layer5Delta = mxjava.scalarMult(layer5Error,sigmoidDerivativeForLayer5);
			
			//layer 4 delta
			double[][] layer4Error = mxjava.matrixMult(layer5Delta,mxjava.transpose(synapse4));
			double[][] sigmoidDerivativeForLayer4 = new double[layer4.length][layer4[0].length];
			for (int i = 0;i<layer1.length;i++) {
				for (int j = 0;j<layer1[0].length;j++) {
					sigmoidDerivativeForLayer4[i][j] = mxjava.sigmoidPackage(layer4[i][j],true);
				}
			}
			double[][] layer4Delta = mxjava.scalarMult(layer4Error,sigmoidDerivativeForLayer4);
			
			
			//layer 3 delta
			double[][] layer3Error = mxjava.matrixMult(layer4Delta,mxjava.transpose(synapse3));
			double[][] sigmoidDerivativeForLayer3 = new double[layer3.length][layer3[0].length];
			for (int i = 0;i<layer1.length;i++) {
				for (int j = 0;j<layer1[0].length;j++) {
					sigmoidDerivativeForLayer3[i][j] = mxjava.sigmoidPackage(layer3[i][j],true);
				}
			}
			double[][] layer3Delta = mxjava.scalarMult(layer3Error,sigmoidDerivativeForLayer3);
			
			
			//layer 2 delta
			double[][] layer2Error = mxjava.matrixMult(layer3Delta,mxjava.transpose(synapse2));
			double[][] sigmoidDerivativeForLayer2 = new double[layer2.length][layer2[0].length];
			for (int i = 0;i<layer1.length;i++) {
				for (int j = 0;j<layer1[0].length;j++) {
					sigmoidDerivativeForLayer2[i][j] = mxjava.sigmoidPackage(layer2[i][j],true);
				}
			}
			double[][] layer2Delta = mxjava.scalarMult(layer2Error,sigmoidDerivativeForLayer2);

			//layer 1 delta
			double[][] layer1Error = mxjava.matrixMult(layer2Delta,mxjava.transpose(synapse1));
			double[][] sigmoidDerivativeForLayer1 = new double[layer1.length][layer1[0].length];
			for (int i = 0;i<layer1.length;i++) {
				for (int j = 0;j<layer1[0].length;j++) {
					sigmoidDerivativeForLayer1[i][j] = mxjava.sigmoidPackage(layer1[i][j],true);
				}
			}
			double[][] layer1Delta = mxjava.scalarMult(layer1Error,sigmoidDerivativeForLayer1);

			
			double[][] finalWeight = mxjava.matrixMult(mxjava.transpose(layer7),finalLayerDelta);
			double[][] weight6 = mxjava.matrixMult(mxjava.transpose(layer6),layer7Delta);
			double[][] weight5 = mxjava.matrixMult(mxjava.transpose(layer5),layer6Delta);
			double[][] weight4 = mxjava.matrixMult(mxjava.transpose(layer4),layer5Delta);
			double[][] weight3 = mxjava.matrixMult(mxjava.transpose(layer3),layer4Delta);
			double[][] weight2 = mxjava.matrixMult(mxjava.transpose(layer2),layer3Delta);
			double[][] weight1 = mxjava.matrixMult(mxjava.transpose(layer1),layer2Delta);
			double[][] weight0 = mxjava.matrixMult(mxjava.transpose(layer0),layer1Delta);
			
			//SCALE
			double[][] scaleFinal = new double[finalWeight.length][finalWeight[0].length];
			for  (int i = 0; i<scaleFinal.length;i++) {
				for (int j = 0;j<scaleFinal[0].length;j++) {
					scaleFinal[i][j] = learningSpeed;
				}
			}
			double[][] scaleHidden = new double[weight1.length][weight1[0].length];
			for  (int i = 0; i<scaleHidden.length;i++) {
				for (int j = 0;j<scaleHidden[0].length;j++) {
					scaleHidden[i][j] = learningSpeed;
				}
			}
			double[][] scale0 = new double[weight0.length][weight0[0].length];
			for  (int i = 0; i<scale0.length;i++) {
				for (int j = 0;j<scale0[0].length;j++) {
					scale0[i][j] = learningSpeed;
				}
			}
			
			finalWeight = mxjava.scalarMult(finalWeight,scaleFinal);
			weight6 = mxjava.scalarMult(weight6,scaleHidden);
			weight5 = mxjava.scalarMult(weight5,scaleHidden);
			weight4 = mxjava.scalarMult(weight4,scaleHidden);
			weight3 = mxjava.scalarMult(weight3,scaleHidden);
			weight2 = mxjava.scalarMult(weight2,scaleHidden);
			weight1 = mxjava.scalarMult(weight1,scaleHidden);
			weight0 = mxjava.scalarMult(weight0,scale0);

			finalSynapse = mxjava.add(finalSynapse,finalWeight);
			synapse6 = mxjava.add(synapse6,weight6);
			synapse5 = mxjava.add(synapse5,weight5);
			synapse4 = mxjava.add(synapse4,weight4);
			synapse3 = mxjava.add(synapse3,weight3);
			synapse2 = mxjava.add(synapse2,weight2);
			synapse1 = mxjava.add(synapse1,weight1);
			synapse0 = mxjava.add(synapse0,weight0);
		}
		//System.out.println("--END TRAINING--");
	}
	public double[] predict(double[] NEW_VALUE) {
		double[][] newData = {NEW_VALUE};
		double[][] rawLayer1 = mxjava.matrixMult(newData,synapse0);
		double[][] layer1 = new double[rawLayer1.length][rawLayer1[0].length];
		for (int i = 0;i<rawLayer1.length;i++) {
			for (int j = 0;j<rawLayer1[0].length;j++) {
				layer1[i][j] = mxjava.sigmoidPackage(rawLayer1[i][j],false);
			}
		}
		double[][] rawLayer2 = mxjava.matrixMult(layer1,synapse1);
		double[][] layer2 = new double[rawLayer2.length][rawLayer2[0].length];
		for (int i = 0;i<rawLayer2.length;i++) {
			for (int j = 0;j<rawLayer2[0].length;j++) {
				layer2[i][j] = mxjava.sigmoidPackage(rawLayer2[i][j],false);
			}
		}			
		double[][] rawLayer3 = mxjava.matrixMult(layer2,synapse2);
		double[][] layer3 = new double[rawLayer3.length][rawLayer3[0].length];
		for (int i = 0;i<rawLayer3.length;i++) {
			for (int j = 0;j<rawLayer3[0].length;j++) {
				layer3[i][j] = mxjava.sigmoidPackage(rawLayer3[i][j],false);
			}
		}
		double[][] rawLayer4 = mxjava.matrixMult(layer3,synapse3);
		double[][] layer4 = new double[rawLayer4.length][rawLayer4[0].length];
		for (int i = 0;i<rawLayer4.length;i++) {
			for (int j = 0;j<rawLayer4[0].length;j++) {
				layer4[i][j] = mxjava.sigmoidPackage(rawLayer4[i][j],false);
			}
		}
		double[][] rawLayer5 = mxjava.matrixMult(layer4,synapse4);
		double[][] layer5 = new double[rawLayer5.length][rawLayer5[0].length];
		for (int i = 0;i<rawLayer5.length;i++) {
			for (int j = 0;j<rawLayer5[0].length;j++) {
				layer5[i][j] = mxjava.sigmoidPackage(rawLayer5[i][j],false);
			}
		}
		double[][] rawLayer6 = mxjava.matrixMult(layer5,synapse5);
		double[][] layer6 = new double[rawLayer6.length][rawLayer6[0].length];
		for (int i = 0;i<rawLayer6.length;i++) {
			for (int j = 0;j<rawLayer6[0].length;j++) {
				layer6[i][j] = mxjava.sigmoidPackage(rawLayer6[i][j],false);
			}
		}
		double[][] rawLayer7 = mxjava.matrixMult(layer6,synapse6);
		double[][] layer7 = new double[rawLayer7.length][rawLayer7[0].length];
		for (int i = 0;i<rawLayer7.length;i++) {
			for (int j = 0;j<rawLayer7[0].length;j++) {
				layer7[i][j] = mxjava.sigmoidPackage(rawLayer7[i][j],false);
			}
		}
		double[][] rawFinalLayer = mxjava.matrixMult(layer7,finalSynapse);
		double[][] finalLayer = new double[rawFinalLayer.length][rawFinalLayer[0].length];
		for (int i = 0;i<rawFinalLayer.length;i++) {
			for (int j = 0;j<rawFinalLayer[0].length;j++) {
				finalLayer[i][j] = mxjava.sigmoidPackage(rawFinalLayer[i][j],false);
			}
		}

		//System.out.println(Arrays.toString(finalLayer[0]));
		return finalLayer[0];

	}

}
