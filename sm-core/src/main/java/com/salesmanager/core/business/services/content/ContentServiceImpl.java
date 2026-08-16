package com.salesmanager.core.business.services.content;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import jakarta.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.cms.content.StaticContentFileManager;
import com.salesmanager.core.business.repositories.content.ContentRepository;
import com.salesmanager.core.business.repositories.content.PageContentRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.content.Content;
import com.salesmanager.core.model.content.ContentDescription;
import com.salesmanager.core.model.content.ContentType;
import com.salesmanager.core.model.content.FileContentType;
import com.salesmanager.core.model.content.InputContentFile;
import com.salesmanager.core.model.content.OutputContentFile;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;

@Service("contentService")
public class ContentServiceImpl extends SalesManagerEntityServiceImpl<Long, Content> implements ContentService {

	private static final Logger LOG = LoggerFactory.getLogger(ContentServiceImpl.class);

	private final ContentRepository contentRepository;
	
	@Autowired
	private PageContentRepository pageContentRepository;
	

	@Inject
	StaticContentFileManager contentFileManager;

	@Inject
	public ContentServiceImpl(ContentRepository contentRepository) {
		super(contentRepository);

		this.contentRepository = contentRepository;
	}

	@Override
	public List<Content> listByType(ContentType contentType, MerchantStore store, Language language)
			throws ServiceException {

		return contentRepository.findByType(contentType, store.getId(), language.getId());
	}

	@Override
	public void delete(Content content) throws ServiceException {

		Content c = this.getById(content.getId());
		super.delete(c);

	}

	@Override
	public Content getByLanguage(Long id, Language language) throws ServiceException {
		return contentRepository.findByIdAndLanguage(id, language.getId());
	}

	@Override
	public List<Content> listByType(List<ContentType> contentType, MerchantStore store, Language language)
			throws ServiceException {

		/*
		 * List<String> contentTypes = new ArrayList<String>(); for (int i = 0;
		 * i < contentType.size(); i++) {
		 * contentTypes.add(contentType.get(i).name()); }
		 */

		return contentRepository.findByTypes(contentType, store.getId(), language.getId());
	}

	@Override
	public List<ContentDescription> listNameByType(List<ContentType> contentType, MerchantStore store,
			Language language) throws ServiceException {

		return contentRepository.listNameByType(contentType, store, language);
	}

	@Override
	public List<Content> listByType(List<ContentType> contentType, MerchantStore store) throws ServiceException {

		return contentRepository.findByTypes(contentType, store.getId());
	}

	@Override
	public Content getByCode(String code, MerchantStore store) throws ServiceException {

		return contentRepository.findByCode(code, store.getId());

	}

	@Override
	public Content getById(Long id) {
		return contentRepository.findOne(id);
	}

	@Override
	public void saveOrUpdate(final Content content) throws ServiceException {

		// save or update (persist and attach entities
		if (content.getId() != null && content.getId() > 0) {
			super.update(content);
		} else {
			super.save(content);
		}

	}

	@Override
	public Content getByCode(String code, MerchantStore store, Language language) throws ServiceException {
		return contentRepository.findByCode(code, store.getId(), language.getId());
	}

	/**
	 * Method responsible for adding content file for given merchant store in
	 * underlying Infinispan tree cache. It will take {@link InputContentFile}
	 * and will store file for given merchant store according to its type. it
	 * can save an image or any type of file (pdf, css, js ...)
	 * 
	 * @param merchantStoreCode
	 *            Merchant store
	 * @param contentFile
	 *            {@link InputContentFile} being stored
	 * @throws ServiceException
	 *             service exception
	 */
	@Override
	public void addContentFile(String merchantStoreCode, InputContentFile contentFile) throws ServiceException {
		Assert.notNull(merchantStoreCode, "Merchant store Id can not be null");
		Assert.notNull(contentFile, "InputContentFile image can not be null");
		Assert.notNull(contentFile.getFileName(), "InputContentFile.fileName can not be null");
		Assert.notNull(contentFile.getFileContentType(), "InputContentFile.fileContentType can not be null");

		String mimeType = URLConnection.guessContentTypeFromName(contentFile.getFileName());
		contentFile.setMimeType(mimeType);

		if (contentFile.getFileContentType().name().equals(FileContentType.IMAGE.name())
				|| contentFile.getFileContentType().name().equals(FileContentType.STATIC_FILE.name())) {
			addFile(merchantStoreCode, contentFile);
		} else if(contentFile.getFileContentType().name().equals(FileContentType.API_IMAGE.name())) {
			contentFile.setFileContentType(FileContentType.IMAGE);
			addImage(merchantStoreCode, contentFile);
		} else if(contentFile.getFileContentType().name().equals(FileContentType.API_FILE.name())) {
			contentFile.setFileContentType(FileContentType.STATIC_FILE);
			addFile(merchantStoreCode, contentFile);
		} else {
			addImage(merchantStoreCode, contentFile);
		}

	}

	@Override
	public void addLogo(String merchantStoreCode, InputContentFile cmsContentImage) throws ServiceException {

		Assert.notNull(merchantStoreCode, "Merchant store Id can not be null");
		Assert.notNull(cmsContentImage, "CMSContent image can not be null");

		cmsContentImage.setFileContentType(FileContentType.LOGO);
		addImage(merchantStoreCode, cmsContentImage);

	}

	@Override
	public void addOptionImage(String merchantStoreCode, InputContentFile cmsContentImage) throws ServiceException {

		Assert.notNull(merchantStoreCode, "Merchant store Id can not be null");
		Assert.notNull(cmsContentImage, "CMSContent image can not be null");
		cmsContentImage.setFileContentType(FileContentType.PROPERTY);
		addImage(merchantStoreCode, cmsContentImage);

	}

	private void addImage(String merchantStoreCode, InputContentFile contentImage) throws ServiceException {

		try {
			LOG.info("Adding content image for merchant id {}", merchantStoreCode);

			String p = contentImage.getPath();
			Optional<String> path = Optional.ofNullable(p);
			contentFileManager.addFile(merchantStoreCode, path, contentImage);

		} catch (Exception e) {
			LOG.error("Error while trying to convert input stream to buffered image", e);
			throw new ServiceException(e);

		} finally {

			try {
				if (contentImage.getFile() != null) {
					contentImage.getFile().close();
				}
			} catch (Exception ignore) {
			}

		}

	}

	private void addFile(final String merchantStoreCode, InputContentFile contentImage) throws ServiceException {

		try {
			LOG.info("Adding content file for merchant id {}", merchantStoreCode);
			// staticContentFileManager.addFile(merchantStoreCode,
			// contentImage);

			String p = null;
			Optional<String> path = Optional.ofNullable(p);

			contentFileManager.addFile(merchantStoreCode, path, contentImage);

		} catch (Exception e) {
			LOG.error("Error while trying to convert input stream to buffered image", e);
			throw new ServiceException(e);

		} finally {

			try {
				if (contentImage.getFile() != null) {
					contentImage.getFile().close();
				}
			} catch (Exception ignore) {
			}
		}

	}

	/**
	 * Method responsible for adding list of content images for given merchant
	 * store in underlying Infinispan tree cache. It will take list of
	 * {@link CMSContentImage} and will store them for given merchant store.
	 * 
	 * @param merchantStoreCode
	 *            Merchant store
	 * @param contentImagesList
	 *            list of {@link CMSContentImage} being stored
	 * @throws ServiceException
	 *             service exception
	 */
	@Override
	public void addContentFiles(String merchantStoreCode, List<InputContentFile> contentFilesList)
			throws ServiceException {

		Assert.notNull(merchantStoreCode, "Merchant store ID can not be null");
		Assert.notEmpty(contentFilesList, "File list can not be empty");
		LOG.info("Adding total {} images for given merchant", contentFilesList.size());

		String p = null;
		Optional<String> path = Optional.ofNullable(p);

		LOG.info("Adding content images for merchant....");
		contentFileManager.addFiles(merchantStoreCode, path, contentFilesList);
		// staticContentFileManager.addFiles(merchantStoreCode,
		// contentFilesList);

		try {
			for (InputContentFile file : contentFilesList) {
				if (file.getFile() != null) {
					file.getFile().close();
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * Method to remove given content image.Images are stored in underlying
	 * system based on there name. Name will be used to search given image for
	 * removal
	 * 
	 * @param contentImage
	 * @param merchantStoreCode
	 *            merchant store
	 * @throws ServiceException
	 */
	@Override
	public void removeFile(String merchantStoreCode, FileContentType fileContentType, String fileName)
			throws ServiceException {
		Assert.notNull(merchantStoreCode, "Merchant Store Id can not be null");
		Assert.notNull(fileContentType, "Content file type can not be null