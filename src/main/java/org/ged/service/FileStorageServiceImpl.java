package org.ged.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.ged.bean.FileStorageException;
import org.ged.bean.MyFileNotFoundException;
import org.ged.entities.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {

	private final Path fileStorageLocation;

	@Autowired
	public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getLocation()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException(
					"Impossible de créer le répertoire dans lequel les fichiers téléchargés seront stockés", ex);
		}
	}

	/**
	 * On recupere le nom du fichier a partir de l'object MultipartFile recu
	 */
	@Override
	public String storeFile(MultipartFile file) {

		// Normaliser le nom du fichier
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Vérifiez si le nom du fichier contient des caractères invalides
			if (fileName.contains("..")) {
				throw new FileStorageException(
						"Pardon! Le nom de fichier contient une séquence de chemin non valide" + fileName);
			}

			// Copier le fichier à l'emplacement cible (remplacement du fichier existant
			// avec le même nom)
			Path targetLocation = this.fileStorageLocation.resolve("erico_" + fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (IOException ex) {
			throw new FileStorageException("Impossible de stocker le fichier " + fileName + ". Please try again!", ex);
		}

	}

	@Override
	public Resource loadFileAsResource(String fileName) {

		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("Fichier introuvable " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("Fichier introuvable " + fileName, ex);
		}
	}

}
