package seleniumUI;

public class ReversalProgram {


    public static void reverse(char str[]) 
    { 
        // Initialize left and right pointers 
        int right = str.length - 1, left = 0; 
  
        // Traverse string from both ends until 
        // 'left' and 'right' 
        while (left < right) 
        { 
            // Ignore special characters 
            if (!Character.isAlphabetic(str[left])) 
                left++; 
            else if(!Character.isAlphabetic(str[right])) 
                right--; 
  
            else 
            { 
                char tmp = str[left]; 
                str[left] = str[right]; 
                str[right] = tmp; 
                left++; 
                 right--; 
            } 
        } 
    } 
  
    // Driver Code 
    public static void main(String[] args)  
    { 
        String str = "newb, &&One!! newa"; 
        char[] charArray = str.toCharArray(); 
        System.out.println("Input string: " + str); 
        reverse(charArray); 
        String revStr = new String(charArray); 
        System.out.println("Output string: " + revStr); 
    }
}
