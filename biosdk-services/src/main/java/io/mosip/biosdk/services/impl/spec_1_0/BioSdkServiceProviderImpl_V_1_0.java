package io.mosip.biosdk.services.impl.spec_1_0;

import static io.mosip.biosdk.services.constants.AppConstants.LOGGER_IDTYPE;
import static io.mosip.biosdk.services.constants.AppConstants.LOGGER_SESSIONID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.mosip.biosdk.services.config.LoggerConfig;
import io.mosip.biosdk.services.constants.ErrorMessages;
import io.mosip.biosdk.services.dto.RequestDto;
import io.mosip.biosdk.services.exceptions.BioSDKException;
import io.mosip.biosdk.services.impl.spec_1_0.dto.request.CheckQualityRequestDto;
import io.mosip.biosdk.services.impl.spec_1_0.dto.request.ConvertFormatRequestDto;
import io.mosip.biosdk.services.impl.spec_1_0.dto.request.ExtractTemplateRequestDto;
import io.mosip.biosdk.services.impl.spec_1_0.dto.request.InitRequestDto;
import io.mosip.biosdk.services.impl.spec_1_0.dto.request.MatchRequestDto;
import io.mosip.biosdk.services.impl.spec_1_0.dto.request.SegmentRequestDto;
import io.mosip.biosdk.services.spi.BioSdkServiceProvider;
import io.mosip.biosdk.services.utils.Utils;
import io.mosip.kernel.biometrics.entities.BiometricRecord;
import io.mosip.kernel.biometrics.model.Response;
import io.mosip.kernel.biometrics.model.SDKInfo;
import io.mosip.kernel.biometrics.spi.IBioApiV2;
import io.mosip.kernel.core.logger.spi.Logger;

@Component
public class BioSdkServiceProviderImpl_V_1_0 // NOSONAR
		implements BioSdkServiceProvider {

	private Logger logger = LoggerConfig.logConfig(BioSdkServiceProviderImpl_V_1_0.class);

	private static final String BIOSDK_SERVICE_SPEC_VERSION = "1.0";
	private static final String INIT = "init";
	private static final String CHECK_QUALITY = "checkQuality";
	private static final String EXTRACT_TEMPLATE = "extractTemplate";
	private static final String MATCH = "match";
	private static final String SEGMENT = "segment";
	private static final String CONVERT_FORMAT = "convertFormat";

	private static final String DECODE_SUCCESS = "decoding successful";
	private static final String JSON_TO_DTO_SUCCESS = "json to dto successful";

	private IBioApiV2 iBioApi;
	private Utils utils;
	private Gson gson;

	@Value("${mosip.biosdk.log-request-response-enabled:false}")
	private boolean isLogRequestResponse;

	@Autowired
	public BioSdkServiceProviderImpl_V_1_0(IBioApiV2 iBioApi, Utils utils) {
		this.iBioApi = iBioApi;
		this.utils = utils;
		gson = new GsonBuilder().serializeNulls().create();
	}

	@Override
	public String getSpecVersion() {
		return BIOSDK_SERVICE_SPEC_VERSION;
	}

	@Override
	public Object init(RequestDto request) {
		SDKInfo sdkInfo = null;
		String decryptedRequest = decode(request.getRequest());
		logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, INIT, DECODE_SUCCESS);
		InitRequestDto initRequestDto = gson.fromJson(decryptedRequest, InitRequestDto.class);
		logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, INIT, JSON_TO_DTO_SUCCESS);
		try {
			logRequest(initRequestDto);
			sdkInfo = iBioApi.init(initRequestDto.getInitParams());
			logObject(sdkInfo);
		} catch (Exception e) {
			logger.error(LOGGER_SESSIONID, LOGGER_IDTYPE, INIT, e);
			throw new BioSDKException(ErrorMessages.BIOSDK_LIB_EXCEPTION.toString(),
					ErrorMessages.BIOSDK_LIB_EXCEPTION.getMessage() + ": " + e.getMessage());
		}
		return sdkInfo;
	}

	@Override
	public Object checkQuality(RequestDto request) {
		Response<?> response;
		String decryptedRequest = decode(request.getRequest());
		logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, CHECK_QUALITY, DECODE_SUCCESS);
		CheckQualityRequestDto checkQualityRequestDto = gson.fromJson(decryptedRequest, CheckQualityRequestDto.class);
		logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, CHECK_QUALITY, JSON_TO_DTO_SUCCESS);
		try {
			logRequest(checkQualityRequestDto);
			response = iBioApi.checkQuality(checkQualityRequestDto.getSample(),
					checkQualityRequestDto.getModalitiesToCheck(), checkQualityRequestDto.getFlags());
			logResponse(response);
		} catch (Exception e) {
			logger.error(LOGGER_SESSIONID, LOGGER_IDTYPE, CHECK_QUALITY, e);
			throw new BioSDKException(ErrorMessages.BIOSDK_LIB_EXCEPTION.toString(),
					ErrorMessages.BIOSDK_LIB_EXCEPTION.getMessage() + ": " + e.toString() + " " + e.getMessage());
		}
		return response;
	}

	@Override
	public Object match(RequestDto request) {
		Response<?> response;
		String decryptedRequest = decode(request.getRequest());
		logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, MATCH, DECODE_SUCCESS);
		MatchRequestDto matchRequestDto = gson.fromJson(decryptedRequest, MatchRequestDto.class);
		logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, MATCH, JSON_TO_DTO_SUCCESS);
		try {
			logRequest(matchRequestDto);
			response = iBioApi.match(matchRequestDto.getSample(), matchRequestDto.getGallery(),
					matchRequestDto.getModalitiesToMatch(), matchRequestDto.getFlags());
			logResponse(response);
		} catch (Exception e) {
			logger.error(LOGGER_SESSIONID, LOGGER_IDTYPE, MATCH, e);
			throw new BioSDKException(ErrorMessages.BIOSDK_LIB_EXCEPTION.toString(),
					ErrorMessages.BIOSDK_LIB_EXCEPTION.getMessage() + ": " + e.toString() + " " + e.getMessage());
		}
		return response;
	}

	@Override
	public Object extractTemplate(RequestDto request) {
		Response<?> response;
		String decryptedRequest = decode(request.getRequest());
		logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, EXTRACT_TEMPLATE, DECODE_SUCCESS);
		ExtractTemplateRequestDto extractTemplateRequestDto = gson.fromJson(decryptedRequest,
				ExtractTemplateRequestDto.class);
		logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, EXTRACT_TEMPLATE, JSON_TO_DTO_SUCCESS);
		try {
			logRequest(extractTemplateRequestDto);
			response = iBioApi.extractTemplate(extractTemplateRequestDto.getSample(),
					extractTemplateRequestDto.getModalitiesToExtract(), extractTemplateRequestDto.getFlags());
			logResponse(response);
		} catch (Exception e) {
			logger.error(LOGGER_SESSIONID, LOGGER_IDTYPE, EXTRACT_TEMPLATE, e);
			throw new BioSDKException(ErrorMessages.BIOSDK_LIB_EXCEPTION.toString(),
					ErrorMessages.BIOSDK_LIB_EXCEPTION.getMessage() + ": " + e.toString() + " " + e.getMessage());
		}
		return response;
	}

	@Override
	public Object segment(RequestDto request) {
		Response<?> response;
		String decryptedRequest = decode(request.getRequest());
		logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, SEGMENT, DECODE_SUCCESS);
		SegmentRequestDto segmentRequestDto = gson.fromJson(decryptedRequest, SegmentRequestDto.class);
		logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, SEGMENT, JSON_TO_DTO_SUCCESS);
		try {
			logRequest(segmentRequestDto);
			response = iBioApi.segment(segmentRequestDto.getSample(), segmentRequestDto.getModalitiesToSegment(),
					segmentRequestDto.getFlags());
			logResponse(response);
		} catch (Exception e) {
			logger.error(LOGGER_SESSIONID, LOGGER_IDTYPE, SEGMENT, e);
			throw new BioSDKException(ErrorMessages.BIOSDK_LIB_EXCEPTION.toString(),
					ErrorMessages.BIOSDK_LIB_EXCEPTION.getMessage() + ": " + e.toString() + " " + e.getMessage());
		}
		return response;
	}

	@Override
	public Object convertFormat(RequestDto request) {
		Response<?> response;
		String decryptedRequest = decode(request.getRequest());
		logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, CONVERT_FORMAT, DECODE_SUCCESS);
		ConvertFormatRequestDto convertFormatRequestDto = gson.fromJson(decryptedRequest,
				ConvertFormatRequestDto.class);
		logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, CONVERT_FORMAT, JSON_TO_DTO_SUCCESS);
		try {
			logRequest(convertFormatRequestDto);
			response = iBioApi.convertFormatV2(convertFormatRequestDto.getSample(),
					convertFormatRequestDto.getSourceFormat(), convertFormatRequestDto.getTargetFormat(),
					convertFormatRequestDto.getSourceParams(), convertFormatRequestDto.getTargetParams(),
					convertFormatRequestDto.getModalitiesToConvert());
			logResponse(response);
		} catch (Exception e) {
			logger.error(LOGGER_SESSIONID, LOGGER_IDTYPE, CONVERT_FORMAT, e);
			throw new BioSDKException(ErrorMessages.BIOSDK_LIB_EXCEPTION.toString(),
					ErrorMessages.BIOSDK_LIB_EXCEPTION.getMessage() + ": " + e.toString() + " " + e.getMessage());
		}
		return response;
	}

	private void logRequest(ExtractTemplateRequestDto extractTemplateRequestDto) {
		if (isLogRequestResponse) {
			logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, "REQUEST::ExtractTemplateRequestDto",
					utils.toString(extractTemplateRequestDto));
		}
	}

	private void logRequest(MatchRequestDto matchRequestDto) {
		if (isLogRequestResponse) {
			logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, "REQUEST:: MatchRequestDto", utils.toString(matchRequestDto));
		}
	}

	private void logRequest(InitRequestDto initRequestDto) {
		if (isLogRequestResponse) {
			logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, "REQUEST:: InitRequestDto", utils.toString(initRequestDto));
		}
	}

	private void logRequest(CheckQualityRequestDto checkQualityRequestDto) {
		if (isLogRequestResponse) {
			logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, "REQUEST:: CheckQualityRequestDto",
					utils.toString(checkQualityRequestDto));
		}
	}

	private void logRequest(SegmentRequestDto segmentRequestDto) {
		if (isLogRequestResponse) {
			logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, "REQUEST:: SegmentRequestDto",
					utils.toString(segmentRequestDto));
		}
	}

	private void logRequest(ConvertFormatRequestDto convertFormatRequestDto) {
		if (isLogRequestResponse) {
			logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, "REQUEST:: ConvertFormatRequestDto",
					utils.toString(convertFormatRequestDto));
		}
	}

	private <T> void logObject(T response) {
		if (isLogRequestResponse) {
			logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, response.getClass(), gson.toJson(response));
		}
	}

	private void logResponse(Response<?> response) {
		if (isLogRequestResponse) {
			Object resp = response.getResponse();
			if (resp instanceof BiometricRecord biometricRecord) {
				logBiometricRecord("Response BiometricRecord: ", biometricRecord);
			} else {
				logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, "Response: ", gson.toJson(resp));
			}
		}
	}

	private void logBiometricRecord(String prefix, BiometricRecord biometricRecord) {
		if (isLogRequestResponse) {
			logger.debug(LOGGER_SESSIONID, LOGGER_IDTYPE, prefix + utils.toString(biometricRecord));
		}
	}

	private String decode(String data) {
		try {
			return Utils.base64Decode(data);
		} catch (RuntimeException e) {
			logger.error(LOGGER_SESSIONID, LOGGER_IDTYPE, ErrorMessages.INVALID_REQUEST_BODY.toString(), e);
			throw new BioSDKException(ErrorMessages.INVALID_REQUEST_BODY.toString(),
					ErrorMessages.INVALID_REQUEST_BODY.getMessage() + ": " + e.toString() + " " + e.getMessage());
		}
	}
}