import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static void main() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int k;
        while  (true){
            System.out.println("@@@@@@@@@@@@@@@ Welcome to the KNN tester. Please, enter a k value to be tested @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");
            System.out.println();
            k = scanner.nextInt();
            if (k > 0){
                break;
            }
        }



        File file = new File("iris.data");
        File file2 = new File("iris.test.data");
        Scanner scan = new Scanner(file);
        Scanner scan2 = new Scanner(file2);

        //Reading data from the iris.data and putting each line into arraylist
        ArrayList<String> data = new ArrayList<>();
        while (scan.hasNextLine()){
            data.add(scan.nextLine());
        }

        //Reading data from the iris.test.data and putting each line into arraylist
        ArrayList<String> data2 = new ArrayList<>();
        while (scan2.hasNextLine()){
            data2.add(scan2.nextLine());
        }

        //Testing methods and the logic by creating 2 arraylist which contains objects.
        ArrayList<Iris> IrisObj = irisListCreator(data);
        ArrayList<Iris> TestObj = irisListCreator(data2);
        int count = 0;


        //Instantiating a Class to keep track of which distance belongs to which species.
        for (int i = 0; i < TestObj.size(); i++){
            ArrayList<DistanceLabel> distanceLabel = new ArrayList<>();
            for (int j = 0 ; j < IrisObj.size(); j++){
                double distance = euclidianDistance(TestObj.get(i).vectors() , IrisObj.get(j).vectors());
                distanceLabel.add(new DistanceLabel(distance, IrisObj.get(j).species()));
            }
            ArrayList<DistanceLabel> sorted = sortDistanceObject(distanceLabel);
            //System.out.println(sorted);
            String testResult = (kNN(sorted,k));
            System.out.println(testResult);


            if (TestObj.get(i).species().equals(testResult)){
                count++;
            }
            System.out.println();
        }

        System.out.println( (double)count / TestObj.size() * 100);


        //User enters 4 vectors into an ArrayList. ArrayList so we can instantiate object without an issue
        System.out.println("Enter EXACTLY 4 vectors to be tested");
        ArrayList<Double> userEntered = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            userEntered.add(scanner.nextDouble());
        }
        System.out.println(userEntered);

        ArrayList<DistanceLabel> userDistance = new ArrayList<>();
        for (int i = 0; i < IrisObj.size(); i++) {
            double distance2 = euclidianDistance(userEntered, IrisObj.get(i).vectors());
            userDistance.add(new DistanceLabel(distance2,IrisObj.get(i).species()));
        }
        ArrayList<DistanceLabel> sorted2 = sortDistanceObject(userDistance);
        String testResult2 = kNN(sorted2,k);
        System.out.println(testResult2);
    }

    //parsing Arraylist into double ArrayList and String so that I can instantiate Iris class
    public static ArrayList<Iris> irisListCreator(ArrayList<String> data){
        ArrayList<Iris> irisList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++ ){
            String[] arr = data.get(i).split(",");
            ArrayList<Double> vectors = new ArrayList<>();
            for (int j = 0; j < arr.length -1 ; j++){
                vectors.add(Double.parseDouble(arr[j]));
            }
            String species = arr[arr.length-1];
            irisList.add(new Iris(vectors,species));
        }
        return irisList;
    }

    public static double euclidianDistance(ArrayList<Double> data , ArrayList<Double> test){
        double distance = 0.0;

        for (int i = 0; i < test.size() ; i++){
            distance += Math.pow(test.get(i)-data.get(i),2);
        }

        return Math.sqrt(distance);
    }

    //Selection Sort for the Record DistanceLabel
    public static ArrayList<DistanceLabel> sortDistanceObject (ArrayList<DistanceLabel> distance){

        for (int i = 0; i < distance.size() - 1; i++){
            int min = i;
            for (int j = i + 1 ; j < distance.size(); j++ ){
                if (distance.get(j).distance() < distance.get(min).distance()){
                    min = j;
                }
            }
            DistanceLabel temp = distance.get(i);
            distance.set(i, distance.get(min));
            distance.set(min, temp);
        }
        return distance;
    }

    //Created a map object to find which species is the closest to our test data.
    public static String kNN (ArrayList<DistanceLabel> distanceObject, int k){

        HashMap<String,Integer> map = new HashMap<String,Integer>();

        for (int i = 0; i < k ; i++){
            if (!(map.containsKey(distanceObject.get(i).label()))){
                map.put(distanceObject.get(i).label(), 1);
            } else {
                map.put(distanceObject.get(i).label(), map.get(distanceObject.get(i).label()) +1);
            }
        }

        String maxKey = "";
        int maxCount = 0;

        for (String i : map.keySet()){
            if (map.get(i) > maxCount){
                maxKey = i;
                maxCount = map.get(i);
            }
        }
        System.out.println(map);
        return maxKey;
    }

}
