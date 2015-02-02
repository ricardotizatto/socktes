package socket;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class AppClient {

    public static void main(String[] args) {
        try {
            System.out.println("Iniciando client...");
            Socket s = new Socket("127.0.0.1", 7777);
            
            serializar(s.getOutputStream(), new Diretorio("D://", true)); 

			List<Diretorio> lista = listarDiretorio(new File("C://temp"), "");

            serializar(s.getOutputStream(), lista); 

            
            for (Diretorio estrutura : lista) {
                if (!estrutura.isDiretorio()) {
                    serializarArquivo(s.getOutputStream(),null, estrutura.getPath());
                    Thread.sleep(1000);
                }
            }
            serializarArquivo(s.getOutputStream(),null, "");
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void serializarArquivo(OutputStream out,ObjectInputStream o2 , String arquivo) {
        Boolean resp = false;
        try {
            ObjectOutputStream o = new ObjectOutputStream(new BufferedOutputStream(out));
            System.out.println("Iniciando Transferencia dos arquivo");
            File f = null;
            if (!arquivo.equals("")) {
                f = new File("C://" + arquivo);
                o.writeUTF(arquivo);
                o.flush();
                
                System.out.println("CLIENT UTF");
                o.writeLong(f.length());
                o.flush();
                
                System.out.println("client Long");
                FileInputStream in = new FileInputStream(f);
                byte[] buf = new byte[4096];
                while (true) {
                    int len = in.read(buf);
                    System.out.println("Carregando: " + len);
                    if (len == -1) {
                        break;
                    }
                    o.write(buf, 0, len);
                    o.flush();
                }
                
            } else {
                o.writeUTF("");
                o.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void serializar(OutputStream out, Object estrutura) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(out));
            oos.writeObject(estrutura);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Diretorio> listarDiretorio(File caminho, String caminhoAnterior) throws Exception {
        List<Diretorio> estrutura = new ArrayList<>();
        Diretorio est = new Diretorio();
        String caminhoArquivo = caminhoAnterior + "/" + caminho.getName();

        if (caminho.isDirectory()) {            
            est.setPath(caminhoArquivo);
            est.setDiretorio(true);
            estrutura.add(est);
            for (File filho : caminho.listFiles()) {
                estrutura.addAll(listarDiretorio(filho, caminhoArquivo));
            }
        } else {
            est.setPath(caminhoArquivo);
            est.setDiretorio(false);
            estrutura.add(est);
        }

        return estrutura;
    }
}
