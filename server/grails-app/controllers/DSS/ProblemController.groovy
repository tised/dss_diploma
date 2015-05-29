package DSS

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

class ProblemController {

    def index() {

        render "we here";
    }

    def saveProblem(){

        //  def problem = new JSONObject(params.problem)
        MaiStorage newUser = new MaiStorage()

        JSONObject jo = new JSONObject(params.problem)
        println jo
        newUser.setProblem(params.problem)
        newUser.setAlternatives(params.alternatives)
        newUser.setCriterias(params.criterias)
        newUser.setIdUser(Integer.valueOf(params.id_user))
        newUser.save(flush: true)

        render "saved"
    }

    def getProblem(){

        def problem = MaiStorage.get(MaiStorage.last().id)

        JSONArray answer = new JSONArray()
        answer.put(problem.problem.replace("\"",""))
        answer.put(new JSONArray(problem.alternatives))
        answer.put(new JSONArray(problem.criterias)   )

        render(contentType: "application/json") {
            answer
        }
    }

    def setExpertResult(){

        /*
        * params:
        * alternative_priority
        * id_expert*/

        def result = new ExpertResults()
        String priority_vector = String.valueOf(params.alternative_priority);


        StringBuilder sb = new StringBuilder(priority_vector)

        sb.deleteCharAt(0)
        sb.deleteCharAt(sb.size()-1)
        String res =  sb.toString();

        result.setIdExpert(Integer.valueOf(params.id_expert))
        result.setResult_vector(res)
        //result.setIdProblem(Integer.valueOf(params.id_problem))
        result.setIdProblem(1)
        result.save(flush: true)

        render "saved result"
    }


    private float[][] unleashMatrix(float[][] arr){

        float[][] toUnleashMatrix = arr;

        def readyElements = [];

        int cols = toUnleashMatrix[0].size(), rows = toUnleashMatrix.length;

        println "rows == " + rows

        float[][] unleashedArray = new float[rows][cols];

        for (int i = 0; i < rows; i++){

            def subArray = toUnleashMatrix[i]

            def sortedSubArray = subArray.toList().sort()

            for (int j = 0; j < cols; j++){

                if ( (readyElements.findAll{it == subArray[j]} == []) && (subArray.findAll{it == subArray[j] !=0}) ) {
                    def tmp1 = sortedSubArray.findIndexValues{it == subArray[j]}.collect{it + 1}
                    def tmp2 = subArray.findIndexValues{it == subArray[j]}
                    for (int k = 0; k < sortedSubArray.findAll{it == subArray[j]}.size(); k++)    {
                       // println "new == " + tmp2[k]
                        unleashedArray[i][Integer.valueOf(tmp2[k].intValue())] = tmp1.sum()/sortedSubArray.findAll{it == subArray[j]}.size();

                    }
                }
                readyElements = [readyElements, subArray[j]]
            }
            readyElements = [];
        }

        return unleashedArray;
    }



    def kemeni(){

        def startArray = [
                [1, 2, 2, 6, 3, 3, 4, 7, 1, 2, 5],
                [1, 4, 3, 6, 11, 10, 7, 9, 2, 5, 8],
                [1, 4, 8, 7, 6, 10, 11, 5, 3, 2, 9],
                [3, 1, 4, 5, 6, 8, 5, 8, 3, 2, 7],
                [7, 9, 4, 6, 5, 3, 2, 8, 3, 1, 10],
                [1, 1, 3, 2, 3, 5, 4, 6, 2, 1, 4],
                [1, 1, 2, 2, 4, 3, 4, 1, 2, 2, 5],
                [1, 2, 3, 5, 3, 3, 4, 2, 3, 4, 6],
                [1, 3, 4, 2, 3, 4, 4, 5, 4, 1, 6],
                [1, 4, 4, 2, 4, 4, 4, 3, 3, 5, 6],
                [2, 5, 5, 6, 7, 7, 7, 3, 4, 1, 8],
                [2, 1, 4, 5, 2, 6, 1, 7, 3, 1, 3],
                [3, 2, 1, 5, 6, 7, 3, 3, 4, 5, 6],
                [3, 2, 4, 2, 6, 7, 5, 6, 4, 1, 8],
                [2, 1, 3, 5, 7, 8, 10, 3, 4, 6, 9],
                [1, 5, 3, 5, 6, 6, 6, 2, 4, 1, 5],
                [1, 4, 4, 9, 7, 8, 6, 2, 5, 3, 10],
                [1, 4, 2, 3, 5, 5, 7, 8, 3, 5, 6]
        ] as float[][];

        startArray = unleashMatrix(startArray)

        //double[][] startArray = [[5, 3.5, 1, 3.5, 2], [4, 5, 2, 3, 1],[ 5, 4, 1, 3, 2,], [5, 3.5, 1.5, 3.5, 1.5], [5, 4, 1, 3, 2],[4.5, 4.5, 2, 3, 1]] ;
        println "start array is === " + startArray

        int cols = startArray[0].size(), rows = startArray.length;
        // println "start cols == "+cols + " start rows == "+ rows

        double[][] pairComp = new float[cols*rows][cols];
        int newRow = 0;
        int tmp = 0;

        for(int i = 0; i < cols*rows; i++)
            for(int j = 0; j < cols; j++)
                pairComp[i][j] = 0;

        // println "cols == " + pairComp[0].size() + " " + " rows " +  pairComp.length;
        for(int i =0; i <rows; i++){

            for(int j = 0; j < cols; j++){

                tmp = startArray[i][j];

                for (int k = 0; k <cols; k++){

                    // println "i == "+i+" j == "+j + "new row == " + newRow

                    if (startArray[i][k] == tmp)
                        pairComp[newRow][k] = 0

                    if (startArray[i][k] < tmp)
                        pairComp[newRow][k] = -1

                    if (startArray[i][k] > tmp)
                        pairComp[newRow][k] = 1
                }
                newRow++;

            }
        }

        println pairComp
        calcKemeni(pairComp)
        render "ready"
    }

    def void calcKemeni(double[][] arr){

        def pairsArr = arr;

        int cols = arr[0].size(), rows = arr.length;
        println "cols == "+cols + " rows == "+rows
        int count = rows/cols;
        def matA = []
        def matB = []
        float[][] kennMat = new float[count][count];

        for(int i = 0; i < count; i++)
            for(int j = 0; j < count; j++)
                kennMat[i][j] = 0;

        int rowStartA = 0;
        int rowEndA = cols;
        int rowStartB = 0;
        int rowEndB = cols;


        for(int i = 0; i < count; i++){

            matA = pairsArr.collect().toList().subList(rowStartA,rowEndA)

            for (int j = 0; j < count; j++){
                matB = pairsArr.collect().toList().subList(rowStartB,rowEndB)
                rowStartB = rowStartB+cols;
                rowEndB = rowEndB + cols;

                kennMat[i][j] = processKem(matA,matB, cols)

            }
            rowStartA = rowStartA + cols;
            rowEndA = rowEndA + cols;
            rowStartB = 0;
            rowEndB = cols;
        }

        println kennMat

        float[] sumArr = new float[count];

        for(int i = 0; i<count;i++){
            for (int j = 0; j < count; j++){

                sumArr[i] += kennMat[j][i];
            }
        }
        println "min == " + sumArr.collect().toList().findIndexOf {it == sumArr.collect().toList().min()}
    }

    def float processKem(def arr1, def arr2, int cols){

        println "arr 1 == " + arr1
        println "arr 2 == " + arr2

        float val = 0;

        //int cols = arr1.getAt(0).collect().size(), rows = arr1.length;


        for (int i = 0; i < cols; i++){
            for (int j = 0; j < cols; j++){

                val += (Math.abs(arr1[i][j]-arr2[i][j]))/2
            }
        }

        //println val
        return val;
    }
}
