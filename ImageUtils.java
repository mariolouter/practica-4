public class ImageUtils {
   public static int [][] brillo (int[][] img, int value){
        for (int i=0;i < img.length;i++){
            for(int j= 0; j<img[0].length;j++){
                img[i][j]= img[i][j]+value;
                if (img[i][j] > 255){
                    img[i][j] = 255;
                }
                if(img[i][j] < 0){
                    img[i][j] = 0;
                }
            }
            
        }
        return img;
   }
   public static int [][] binarizar(int[][] img, int value){
     for (int i=0;i < img.length;i++){
            for(int j= 0; j<img[0].length;j++){
                
                if (img[i][j] > value){
                    img[i][j] = 255;
                }else{
                    img[i][j] = 0;
                }
            }
            
        }
     return img;
   }

   private static int mediaSubMatriz (int [][] img, int row, int col, int w){  
        int valores=0;
        int totalPixeles = (row+w - row + 1) * (col+w - col + 1);
         for (int i = row; i <= row+w; i++) {
            for (int j = col; j <= col+w; j++) {
               
                valores += img [i][j];
            
          
           
           }
        
    }
    int mediaSubMatriz = valores/totalPixeles;
    return mediaSubMatriz;
  } 
  private static void ponerValoresSubmatriz (int [][] img, int row, int col, int w, int value){
     
      for (int i = row; i <= row+w && i < img.length; i++) {
            for (int j = col; j <= col+w && j < img[0].length; j++) {
                img[j][i] = value;
  

            }
      }
      
  }
  public static int [][] pixelar (int [][] img, int w){
      for (int x = 0; x < img.length && x < img.length+w; x += w) {
            for (int y = 0; y < img[0].length && y < img[0].length + w  ; y += w) {
                int value = mediaSubMatriz(img,x,y,w);
                if(x<=img.length && y<= img[0].length){
                ponerValoresSubmatriz(img,x,y,w,value);
            }
    
   
      }
     
     }
     return img ;
}
}  