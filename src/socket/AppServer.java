package socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class AppServer {

	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(7777);
			System.out.println("Server na porta 7777.");
			while (true) {
				Socket client = ss.accept();

				ObjectInputStream ois = new ObjectInputStream(
						new BufferedInputStream(client.getInputStream()));
				Diretorio destino = (Diretorio) ois.readObject();

				ois = new ObjectInputStream(new BufferedInputStream(
						client.getInputStream()));
				List<Diretorio> lista = (List<Diretorio>) ois.readObject();

				File fileDir;
				System.out.println("Copiando a estrutura");

				for (Diretorio a : lista) {
					if (a.isDiretorio()) {
						System.out.println(destino.getPath() + a.getPath());
						fileDir = new File(destino.getPath() + a.getPath());
						if (!fileDir.exists()) {
							fileDir.mkdir();
						}
					}
				}

				ObjectOutputStream o2 = new ObjectOutputStream(
						new BufferedOutputStream(client.getOutputStream()));
				o2.writeBoolean(true);
				while (true) {
					try {

						ObjectInputStream o = new ObjectInputStream(
								new BufferedInputStream(client.getInputStream()));
						String file = o.readUTF();

						o2.writeBoolean(true);

						if (file.equals("")) {
							break;
						}

						long size = o.readLong();

						o2.writeBoolean(true);

						FileOutputStream fos = new FileOutputStream(
								destino.getPath() + file);
						byte[] buf = new byte[4096];
						while (true) {
							int len = o.read(buf);
							fos.write(buf, 0, len);
							if ((len == -1) || (len == size)) {
								break;
							}
						}
						fos.flush();

						o2.writeBoolean(true);
					} catch (Exception e) {

					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
