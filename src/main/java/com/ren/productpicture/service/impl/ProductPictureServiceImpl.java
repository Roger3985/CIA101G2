package com.ren.productpicture.service.impl;

import com.ren.administrator.entity.Administrator;
import com.ren.productpicture.entity.ProductPicture;
import com.ren.product.dao.ProductRepository;
import com.ren.productpicture.dao.ProductPictureRepository;
import com.ren.productpicture.service.ProductPictureService_interface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ProductPictureServiceImpl implements ProductPictureService_interface {

	@Autowired
	private ProductPictureRepository productPictureRepository;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	public void sendProgress(String sessionId, int progress) {
		messagingTemplate.convertAndSendToUser(sessionId, "/topic/progress", progress);
	}

	/**
	 * 新增商品照片
	 *
	 * @param productPicture 欲新增之商品照片Entity
	 * @return 返回新增後Entity
	 */
	@Override
	public ProductPicture addProductPicture(ProductPicture productPicture) {
		return productPictureRepository.save(productPicture);
	}

	/**
	 *
	 *
	 * @param productPicNo
	 * @return
	 */
	@Override
	public ProductPicture getOneProductPicture(Integer productPicNo) {
		// TODO Auto-generated method stub
		return productPictureRepository.findById(productPicNo).orElse(null);
	}

	/**
	 *
	 *
	 * @param productNo
	 * @return
	 */
	@Override
	public List<ProductPicture> getByProductNo(Integer productNo) {
		return productPictureRepository.findProductPicturesByProduct_ProductNo(productNo);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public List<ProductPicture> getAll() {
		return productPictureRepository.findAll();
	}

	/**
	 *
	 *
	 * @param productPicture 修改後商品照片Entity
	 * @return
	 */
	@Override
	public ProductPicture updateProductPicture(ProductPicture productPicture) {
		return productPictureRepository.save(productPicture);
	}

	/**
	 *
	 *
	 * @param productPicNo 依商品照片編號刪除
	 */
	@Override
	public void deleteProductPicture(Integer productPicNo) {
		productPictureRepository.deleteById(productPicNo);
	}

	/**
	 * 刪除單項商品的所有照片
	 *
	 * @param productNo 欲刪除之商品編號
	 */
	@Override
	public void deletByProductNo(Integer productNo) {
		productPictureRepository.deleteProductPicturesByProduct_ProductNo(productNo);
	}

	public void storeFile(byte[] fileData, ProductPicture productPicture) throws IOException {
		byte[] compressedData = compress(fileData); // Compress the data

		productPicture.setProductPic(compressedData); // Set the compressed data to the file entity
		updateProductPicture(productPicture); // Save the file entity to the database
	}

	public byte[] retrieveFile(Integer productPicNo) {
		ProductPicture productPicture = getOneProductPicture(productPicNo); // Retrieve the file entity by ID
		byte[] productPic = null;
		if ((productPic = productPicture.getProductPic()) != null) {
			return decompress(productPic); // Decompress and return the data
		}
		return null;
	}

	// Helper method to compress data
	private byte[] compress(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();
		byte[] buffer = new byte[1024];
		int compressedDataLength;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			while (!deflater.finished()) {
				compressedDataLength = deflater.deflate(buffer); // Compress data
				baos.write(buffer, 0, compressedDataLength); // Write compressed data to output stream
			}
			return baos.toByteArray(); // Return compressed data as byte array
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// Helper method to decompress data
	private byte[] decompress(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		byte[] buffer = new byte[1024];
		int decompressedDataLength;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			while (!inflater.finished()) {
				decompressedDataLength = inflater.inflate(buffer); // Decompress data
				baos.write(buffer, 0, decompressedDataLength); // Write decompressed data to output stream
			}
			return baos.toByteArray(); // Return decompressed data as byte array
		} catch (IOException | DataFormatException e) {
			throw new RuntimeException(e);
		}
	}
}
