public class merging {

    public static void main(String[] args){
        int[] mergeMe = new int[]{10,9,1,11,4};
        int[] helper = new int[mergeMe.length];
        mergeSort(mergeMe, helper, 0, mergeMe.length-1);

        for(int i = 0; i < mergeMe.length; i++){
            System.out.print(mergeMe[i] + " , ");
        }
    }


    public static void mergeSort(int[] arr, int[] helper, int low, int high){
        if(low < high){
            int middle = (low+high)/2;
            mergeSort(arr, helper, low, middle);
            mergeSort(arr, helper, middle+1, high);
            merge(helper, arr, low, middle, high);
        }
    }

    public static void merge(int[] helper, int[] arr, int low, int middle, int high){
        for(int i = low; i <= high; i++){
            helper[i] = arr[i];
        }

        int hL = low;
        int hR = middle+1;
        int curr = low;

        while(hL <= middle && hR <= high){
            if(helper[hR] <= helper[hL]){
                arr[curr] = helper[hR];
                hR++;
            }else{
                arr[curr] = helper[hL];
                hL++;
            }
            curr++;
        }

        int lRem = middle - hL;
        if(lRem >= 0){
            for(int i = 0; i <= lRem; i++){
                arr[curr++] = helper[hL++];
            }
        }
    }
}
