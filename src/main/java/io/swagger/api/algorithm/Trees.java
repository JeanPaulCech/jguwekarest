package io.swagger.api.algorithm;

import io.swagger.annotations.*;
import io.swagger.api.AlgorithmService;
import io.swagger.api.NotFoundException;
import io.swagger.api.factories.AlgorithmFactory;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;

import static io.swagger.api.Constants.SAVE_MODEL_NOTE;
import static io.swagger.api.Constants.TEXT_URILIST;


@Path("/algorithm")

@Api(description = "Trees algorithm API")

public class Trees  {
    private final AlgorithmService delegate;

    public Trees(@Context ServletConfig servletContext) {
        AlgorithmService delegate = null;

        if (servletContext != null) {
            String implClass = servletContext.getInitParameter("AlgorithmApi.implementation");
            if (implClass != null && !"".equals(implClass.trim())) {
                try {
                    delegate = (AlgorithmService) Class.forName(implClass).newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (delegate == null) {
            delegate = AlgorithmFactory.getAlgorithm();
        }
        this.delegate = delegate;
    }


    @Context ServletContext servletContext;
    @POST
    @Path("/J48")
    @Consumes({ "multipart/form-data" })
    @Produces({ TEXT_URILIST, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "REST interface to WEKA J48 classifier.",
        notes = "REST interface to WEKA J48 classifier. " + SAVE_MODEL_NOTE,
        tags={ "algorithm" },
        extensions = {
            @Extension(properties = {@ExtensionProperty(name = "orn-@id",  value = "/algorithm/J48")}),
            @Extension(properties = {@ExtensionProperty(name = "orn-@type",  value = "x-orn:Algorithm")}),
            @Extension(name = "orn:expects", properties = { @ExtensionProperty(name = "x-orn-@id",  value = "x-orn:Dataset") }),
            @Extension(name = "orn:returns", properties = { @ExtensionProperty(name = "x-orn-@id",  value = "x-orn:Model") }),
            @Extension(name = "algorithm", properties = {
                @ExtensionProperty(name = "J48", value = "https://en.wikipedia.org/wiki/C4.5_algorithm#Implementations")
            })
        })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Resource Not Found") })
    public Response algorithmJ48Post(
        @FormDataParam("file") InputStream fileInputStream,
        @FormDataParam("file") FormDataContentDisposition fileDetail,
        @ApiParam(value = "Dataset URI or local dataset ID (to the arff representation of a dataset).")@FormDataParam("datasetURI")  String datasetUri,
        @ApiParam(value = "Whether to use binary splits on nominal attributes when building the trees.", allowableValues = "0, 1", defaultValue="0")@FormDataParam("binarySplits") Integer binarySplits,
        @ApiParam(value = "The confidence factor used for pruning (smaller values incur more pruning).", defaultValue = "0.25")@FormDataParam("confidenceFactor") BigDecimal confidenceFactor,
        @ApiParam(value = "The minimum number of instances per leaf.", defaultValue = "2")@FormDataParam("minNumObj") Integer minNumObj,
        @ApiParam(value = "Determines the amount of data used for reduced-error pruning.  One fold is used for pruning, the rest for growing the tree", defaultValue = "3")@FormDataParam("numFolds") Integer numFolds,
        @ApiParam(value = "Whether reduced-error pruning is used instead of C.4.5 pruning.", allowableValues="0, 1", defaultValue = "0")@FormDataParam("reducedErrorPruning") Integer reducedErrorPruning,
        @ApiParam(value = "The seed used for randomizing the data when reduced-error pruning is used.", defaultValue = "1")@FormDataParam("seed") Integer seed,
        @ApiParam(value = "Whether to consider the subtree raising operation when pruning.", allowableValues="0, 1", defaultValue = "1")@FormDataParam("subtreeRaising") Integer subtreeRaising,
        @ApiParam(value = "Whether pruning is performed.", defaultValue = "1", allowableValues="0, 1")@FormDataParam("unpruned") Integer unpruned,
        @ApiParam(value = "Whether counts at leaves are smoothed based on Laplace.", defaultValue = "0", allowableValues="0, 1")@FormDataParam("useLaplace") Integer useLaplace,
        @ApiParam(value = "Validation to use.", allowableValues = "CrossValidation,Hold-Out", defaultValue = "CrossValidation") @FormDataParam("validation") String validation,
        @ApiParam(value = "Num of Crossvalidations or Percentage Split %.", defaultValue = "10") @FormDataParam("validationNum") Double validationNum,
        @ApiParam(value = "Authorization token" )@HeaderParam("subjectid") String subjectid,
        @Context UriInfo ui, @Context HttpHeaders headers, @Context SecurityContext securityContext)
        throws NotFoundException, IOException {

        HashMap<String, Object> params = new HashMap<>();
        params.put("datasetUri", datasetUri);
        params.put("binarySplits", binarySplits);
        params.put("confidenceFactor", confidenceFactor);
        params.put("minNumObj", minNumObj);
        params.put("numFolds", numFolds);
        params.put("reducedErrorPruning", reducedErrorPruning);
        params.put("seed", seed);
        params.put("subtreeRaising", subtreeRaising);
        params.put("unpruned", unpruned);
        params.put("useLaplace", useLaplace);

        return delegate.algorithmPost(fileInputStream, fileDetail, datasetUri,"J48", params,
                                      validation, validationNum, headers, ui, securityContext);
    }


    @POST
    @Path("/J48/adaboost")
    @Consumes({ "multipart/form-data" })
    @Produces({ TEXT_URILIST, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "REST interface to WEKA Adaboost M1 with J48 classifier.",
        notes = "REST interface to WEKA Adaboost M1 with J48 classifier. " + SAVE_MODEL_NOTE,
        tags = {"algorithm","meta algorithm"} ,
        extensions = {
            @Extension(properties = {@ExtensionProperty(name = "orn-@id",  value = "/algorithm/J48/adaboost")}),
            @Extension(properties = {@ExtensionProperty(name = "orn-@type",  value = "x-orn:Algorithm")}),
            @Extension(name = "orn:expects", properties = { @ExtensionProperty(name = "x-orn-@id",  value = "x-orn:Dataset")}),
            @Extension(name = "orn:returns", properties = { @ExtensionProperty(name = "x-orn-@id",  value = "x-orn:Model")}),
            @Extension(name = "algorithm", properties = {
                @ExtensionProperty(name = "Adaboost M1 meta algorithm", value = "https://en.wikipedia.org/wiki/AdaBoost")
            })
        })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Resource Not Found")})
    public Response algorithmJ48AdaBoostPost(
        //data params
        @FormDataParam("file") InputStream fileInputStream,
        @FormDataParam("file") FormDataContentDisposition fileDetail,@ApiParam(value = "Dataset URI or local dataset ID (to the arff representation of a dataset).") @FormDataParam("datasetURI") String datasetUri,
        //meta params,
        @ApiParam(value = "Adaboost M1: The preferred number of instances to process if batch prediction is being performed. More or fewer instances may be provided, but this gives implementations a chance to specify a preferred batch size.", defaultValue = "100") @FormDataParam("batchSize") Integer batchSize,
        @ApiParam(value = "Adaboost M1: The number of iterations to be performed.", defaultValue = "10") @FormDataParam("numIterations") Integer numIterations,
        @ApiParam(value = "Adaboost M1: Whether resampling is used instead of reweighting.", defaultValue = "0", allowableValues = "0, 1") @FormDataParam("useResampling") Integer useResampling,
        @ApiParam(value = "Adaboost M1: Weight threshold for weight pruning.", defaultValue = "100") @FormDataParam("weightThreshold") Integer weightThreshold,
        //J48,
        @ApiParam(value = "Whether to use binary splits on nominal attributes when building the trees.", allowableValues = "0, 1", defaultValue = "0") @FormDataParam("binarySplits") Integer binarySplits,
        @ApiParam(value = "The confidence factor used for pruning (smaller values incur more pruning).", defaultValue = "0.25") @FormDataParam("confidenceFactor") BigDecimal confidenceFactor,
        @ApiParam(value = "The minimum number of instances per leaf.", defaultValue = "2") @FormDataParam("minNumObj") Integer minNumObj,
        @ApiParam(value = "Determines the amount of data used for reduced-error pruning.  One fold is used for pruning, the rest for growing the tree", defaultValue = "3") @FormDataParam("numFolds") Integer numFolds,
        @ApiParam(value = "Whether reduced-error pruning is used instead of C.4.5 pruning.", allowableValues = "0, 1", defaultValue = "0") @FormDataParam("reducedErrorPruning") Integer reducedErrorPruning,
        @ApiParam(value = "The seed used for randomizing the data when reduced-error pruning is used.", defaultValue = "1") @FormDataParam("seed") Integer seed,
        @ApiParam(value = "Whether to consider the subtree raising operation when pruning.", allowableValues = "0, 1", defaultValue = "1") @FormDataParam("subtreeRaising") Integer subtreeRaising,
        @ApiParam(value = "Whether pruning is performed.", defaultValue = "1", allowableValues = "0, 1") @FormDataParam("unpruned") Integer unpruned,
        @ApiParam(value = "Whether counts at leaves are smoothed based on Laplace.", defaultValue = "0", allowableValues = "0, 1") @FormDataParam("useLaplace") Integer useLaplace,
        //general params,
        @ApiParam(value = "Validation to use.", allowableValues = "CrossValidation,Hold-Out", defaultValue = "CrossValidation") @FormDataParam("validation") String validation,
        @ApiParam(value = "Num of Crossvalidations or Percentage Split %.", defaultValue = "10") @FormDataParam("validationNum") Double validationNum,
        @ApiParam(value = "Authorization token") @HeaderParam("subjectid") String subjectid,
        @Context UriInfo ui, @Context HttpHeaders headers, @Context SecurityContext securityContext)
        throws NotFoundException, IOException {

        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> metaParams = new HashMap<>();
        metaParams.put("batchSize", batchSize);
        metaParams.put("numIterations", numIterations);
        metaParams.put("useResampling", useResampling);
        metaParams.put("weightThreshold", weightThreshold);
        params.put("datasetUri", datasetUri);
        params.put("binarySplits", binarySplits);
        params.put("confidenceFactor", confidenceFactor);
        params.put("minNumObj", minNumObj);
        params.put("numFolds", numFolds);
        params.put("reducedErrorPruning", reducedErrorPruning);
        params.put("seed", seed);
        params.put("subtreeRaising", subtreeRaising);
        params.put("unpruned", unpruned);
        params.put("useLaplace", useLaplace);

        return delegate.algorithmPost(fileInputStream, fileDetail, datasetUri,"J48", params,
            "AdaBoost", metaParams, validation, validationNum, headers, ui, securityContext);

    }


    @POST
    @Path("/J48/bagging")
    @Consumes({ "multipart/form-data" })
    @Produces({ TEXT_URILIST, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "REST interface to WEKA Bagging with J48 classifier.",
        notes = "REST interface to WEKA Bagging with J48 classifier. " + SAVE_MODEL_NOTE,
        tags = {"algorithm","meta algorithm"},
        extensions = {
        @Extension(properties = {@ExtensionProperty(name = "orn-@id",  value = "/algorithm/J48/bagging")}),
        @Extension(properties = {@ExtensionProperty(name = "orn-@type",  value = "x-orn:Algorithm")}),
        @Extension(name = "orn:expects", properties = { @ExtensionProperty(name = "x-orn-@id",  value = "x-orn:Dataset")}),
        @Extension(name = "orn:returns", properties = { @ExtensionProperty(name = "x-orn-@id",  value = "x-orn:Model")}),
        @Extension(name = "algorithm", properties = {@ExtensionProperty(name = "Bagging meta algorithm", value = "https://en.wikipedia.org/wiki/Bootstrap_aggregating")})
    })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Resource Not Found")})
    public Response algorithmJ48BaggingPost(
        //data params
        @FormDataParam("file") InputStream fileInputStream,
        @FormDataParam("file") FormDataContentDisposition fileDetail,
        @ApiParam(value = "Dataset URI or local dataset ID (to the arff representation of a dataset).") @FormDataParam("datasetURI") String datasetUri,
        //meta params,
        @ApiParam(value = "Bagging: Size of each bag, as a percentage of the training set size.", defaultValue = "100") @FormDataParam("bagSizePercent") Integer bagSizePercent,
        @ApiParam(value = "Bagging: The preferred number of instances to process if batch prediction is being performed. More or fewer instances may be provided, but this gives implementations a chance to specify a preferred batch size.", defaultValue = "100") @FormDataParam("batchSize") Integer batchSize,
        @ApiParam(value = "Bagging: The number of iterations to be performed.", defaultValue = "10") @FormDataParam("numIterations") Integer numIterations,
        //J48,
        @ApiParam(value = "Whether to use binary splits on nominal attributes when building the trees.", allowableValues = "0, 1", defaultValue = "0") @FormDataParam("binarySplits") Integer binarySplits,
        @ApiParam(value = "The confidence factor used for pruning (smaller values incur more pruning).", defaultValue = "0.25") @FormDataParam("confidenceFactor") BigDecimal confidenceFactor,
        @ApiParam(value = "The minimum number of instances per leaf.", defaultValue = "2") @FormDataParam("minNumObj") Integer minNumObj,
        @ApiParam(value = "Determines the amount of data used for reduced-error pruning.  One fold is used for pruning, the rest for growing the tree", defaultValue = "3") @FormDataParam("numFolds") Integer numFolds,
        @ApiParam(value = "Whether reduced-error pruning is used instead of C.4.5 pruning.", allowableValues = "0, 1", defaultValue = "0") @FormDataParam("reducedErrorPruning") Integer reducedErrorPruning,
        @ApiParam(value = "The seed used for randomizing the data when reduced-error pruning is used.", defaultValue = "1") @FormDataParam("seed") Integer seed,
        @ApiParam(value = "Whether to consider the subtree raising operation when pruning.", allowableValues = "0, 1", defaultValue = "1") @FormDataParam("subtreeRaising") Integer subtreeRaising,
        @ApiParam(value = "Whether pruning is performed.", defaultValue = "1", allowableValues = "0, 1") @FormDataParam("unpruned") Integer unpruned,
        @ApiParam(value = "Whether counts at leaves are smoothed based on Laplace.", defaultValue = "0", allowableValues = "0, 1") @FormDataParam("useLaplace") Integer useLaplace,
        //general params,
        @ApiParam(value = "Validation to use.", allowableValues = "CrossValidation,Hold-Out", defaultValue = "CrossValidation") @FormDataParam("validation") String validation,
        @ApiParam(value = "Num of Crossvalidations or Percentage Split %.", defaultValue = "10") @FormDataParam("validationNum") Double validationNum,
        @ApiParam(value = "Authorization token") @HeaderParam("subjectid") String subjectid,
        @Context UriInfo ui, @Context HttpHeaders headers, @Context SecurityContext securityContext)
        throws NotFoundException, IOException {

        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> metaParams = new HashMap<>();
        metaParams.put("bagSizePercent", bagSizePercent);
        metaParams.put("batchSize", batchSize);
        metaParams.put("numIterations", numIterations);
        params.put("datasetUri", datasetUri);
        params.put("binarySplits", binarySplits);
        params.put("confidenceFactor", confidenceFactor);
        params.put("minNumObj", minNumObj);
        params.put("numFolds", numFolds);
        params.put("reducedErrorPruning", reducedErrorPruning);
        params.put("seed", seed);
        params.put("subtreeRaising", subtreeRaising);
        params.put("unpruned", unpruned);
        params.put("useLaplace", useLaplace);

        return delegate.algorithmPost(fileInputStream, fileDetail, datasetUri,"J48", params,
            "Bagging", metaParams, validation, validationNum, headers, ui, securityContext);
    }

    @POST
    @Path("/M5P")
    @Consumes({ "multipart/form-data" })
    @Produces({ TEXT_URILIST, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "REST interface to WEKA M5P classifier.",
        notes = "REST interface to WEKA M5P classifier. " + SAVE_MODEL_NOTE,
        tags = {"algorithm"} )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Resource Not Found")})
    public Response algorithmM5PPost(
        //data params
        @FormDataParam("file") InputStream fileInputStream,
        @FormDataParam("file") FormDataContentDisposition fileDetail,
        @ApiParam(value = "Dataset URI or local dataset ID (to the arff representation of a dataset).") @FormDataParam("datasetURI") String datasetUri,
        // M5P
        @ApiParam(value = "Whether unpruned tree to be generated.", example = "0", defaultValue = "0", allowableValues="0,1")@FormDataParam("unpruned") Integer unpruned,
        @ApiParam(value = "Whether to use unsmoothed predictions.", defaultValue = "0", allowableValues="0,1")@FormDataParam("useUnsmoothed") Integer useUnsmoothed,
        @ApiParam(value = "The minimum number of instances to allow at a leaf node.", defaultValue = "4")@FormDataParam("minNumInstances") Double minNumInstances,
        @ApiParam(value = "Whether to generate a regression tree/rule instead of a model tree/rule.", defaultValue = "0", allowableValues="0,1")@FormDataParam("buildRegressionTree") Integer buildRegressionTree,
        @ApiParam(value = "Validation to use.", allowableValues = "CrossValidation,Hold-Out", defaultValue = "CrossValidation") @FormDataParam("validation") String validation,
        @ApiParam(value = "Num of Crossvalidations or Percentage Split %.", defaultValue = "10") @FormDataParam("validationNum") Double validationNum,
        @ApiParam(value = "Authorization token" )@HeaderParam("subjectid") String subjectid,
        @Context UriInfo ui, @Context HttpHeaders headers, @Context SecurityContext securityContext)
        throws NotFoundException, IOException {

        HashMap<String, Object> params = new HashMap<>();
        params.put("datasetUri", datasetUri);
        params.put("minNumInstances", minNumInstances);
        params.put("unpruned", unpruned);
        params.put("useUnsmoothed", useUnsmoothed);
        params.put("buildRegressionTree", buildRegressionTree);
        return delegate.algorithmPost(fileInputStream, fileDetail, datasetUri, "M5P", params,
            validation, validationNum, headers, ui, securityContext);
    }

    @POST
    @Path("/M5P/adaboost")
    @Consumes({ "multipart/form-data" })
    @Produces({ TEXT_URILIST, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "REST interface to WEKA AdaBoost M1 with M5P classifier.",
        notes = "REST interface to WEKA AdaBoost M1 with M5P classifier. " + SAVE_MODEL_NOTE,
        tags = {"algorithm","meta algorithm"} )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Resource Not Found")})
    public Response algorithmM5PAdaBoostPost(
        //data params
        @FormDataParam("file") InputStream fileInputStream,
        @FormDataParam("file") FormDataContentDisposition fileDetail,
        @ApiParam(value = "Dataset URI or local dataset ID (to the arff representation of a dataset).") @FormDataParam("datasetURI") String datasetUri,
        //meta params,
        @ApiParam(value = "Adaboost M1: The preferred number of instances to process if batch prediction is being performed. More or fewer instances may be provided, but this gives implementations a chance to specify a preferred batch size.", defaultValue = "100") @FormDataParam("batchSize") Integer batchSize,
        @ApiParam(value = "Adaboost M1: The number of iterations to be performed.", defaultValue = "10") @FormDataParam("numIterations") Integer numIterations,
        @ApiParam(value = "Adaboost M1: Whether resampling is used instead of reweighting.", defaultValue = "0", allowableValues = "0, 1") @FormDataParam("useResampling") Integer useResampling,
        @ApiParam(value = "Adaboost M1: Weight threshold for weight pruning.", defaultValue = "100") @FormDataParam("weightThreshold") Integer weightThreshold,
        // M5P
        @ApiParam(value = "Whether unpruned tree to be generated.", example = "0", defaultValue = "0", allowableValues="0,1")@FormDataParam("unpruned") Integer unpruned,
        @ApiParam(value = "Whether to use unsmoothed predictions.", defaultValue = "0", allowableValues="0,1")@FormDataParam("useUnsmoothed") Integer useUnsmoothed,
        @ApiParam(value = "The minimum number of instances to allow at a leaf node.", defaultValue = "4")@FormDataParam("minNumInstances") Double minNumInstances,
        @ApiParam(value = "Whether to generate a regression tree/rule instead of a model tree/rule.", defaultValue = "0", allowableValues="0,1")@FormDataParam("buildRegressionTree") Integer buildRegressionTree,
        @ApiParam(value = "Validation to use.", allowableValues = "CrossValidation,Hold-Out", defaultValue = "CrossValidation") @FormDataParam("validation") String validation,
        @ApiParam(value = "Num of Crossvalidations or Percentage Split %.", defaultValue = "10") @FormDataParam("validationNum") Double validationNum,
        @ApiParam(value = "Authorization token" )@HeaderParam("subjectid") String subjectid,
        @Context UriInfo ui, @Context HttpHeaders headers, @Context SecurityContext securityContext)
        throws NotFoundException, IOException {

        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> metaParams = new HashMap<>();
        metaParams.put("batchSize", batchSize);
        metaParams.put("numIterations", numIterations);
        metaParams.put("useResampling", useResampling);
        metaParams.put("weightThreshold", weightThreshold);
        params.put("datasetUri", datasetUri);
        params.put("minNumInstances", minNumInstances);
        params.put("unpruned", unpruned);
        params.put("useUnsmoothed", useUnsmoothed);
        params.put("buildRegressionTree", buildRegressionTree);
        return delegate.algorithmPost(fileInputStream, fileDetail, datasetUri, "M5P", params,
            "AdaBoost", metaParams, validation, validationNum, headers, ui, securityContext);
    }

    @POST
    @Path("/M5P/bagging")
    @Consumes({ "multipart/form-data" })
    @Produces({ TEXT_URILIST, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "REST interface to WEKA Bagging with M5P classifier.",
        notes = "REST interface to WEKA Bagging with M5P classifier. " + SAVE_MODEL_NOTE,
        tags = {"algorithm","meta algorithm"} )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Resource Not Found")})
    public Response algorithmM5PBaggingPost(
        //data params
        @FormDataParam("file") InputStream fileInputStream,
        @FormDataParam("file") FormDataContentDisposition fileDetail,
        @ApiParam(value = "Dataset URI or local dataset ID (to the arff representation of a dataset).") @FormDataParam("datasetURI") String datasetUri,
        //meta params,
        @ApiParam(value = "Bagging: Size of each bag, as a percentage of the training set size.", defaultValue = "100") @FormDataParam("bagSizePercent") Integer bagSizePercent,
        @ApiParam(value = "Bagging: The preferred number of instances to process if batch prediction is being performed. More or fewer instances may be provided, but this gives implementations a chance to specify a preferred batch size.", defaultValue = "100") @FormDataParam("batchSize") Integer batchSize,
        @ApiParam(value = "Bagging: The number of iterations to be performed.", defaultValue = "10") @FormDataParam("numIterations") Integer numIterations,
        // M5P
        @ApiParam(value = "Whether unpruned tree to be generated.", example = "0", defaultValue = "0", allowableValues="0,1")@FormDataParam("unpruned") Integer unpruned,
        @ApiParam(value = "Whether to use unsmoothed predictions.", defaultValue = "0", allowableValues="0,1")@FormDataParam("useUnsmoothed") Integer useUnsmoothed,
        @ApiParam(value = "The minimum number of instances to allow at a leaf node.", defaultValue = "4")@FormDataParam("minNumInstances") Double minNumInstances,
        @ApiParam(value = "Whether to generate a regression tree/rule instead of a model tree/rule.", defaultValue = "0", allowableValues="0,1")@FormDataParam("buildRegressionTree") Integer buildRegressionTree,
        @ApiParam(value = "Validation to use.", allowableValues = "CrossValidation,Hold-Out", defaultValue = "CrossValidation") @FormDataParam("validation") String validation,
        @ApiParam(value = "Num of Crossvalidations or Percentage Split %.", defaultValue = "10") @FormDataParam("validationNum") Double validationNum,
        @ApiParam(value = "Authorization token" )@HeaderParam("subjectid") String subjectid,
        @Context UriInfo ui, @Context HttpHeaders headers, @Context SecurityContext securityContext)
        throws NotFoundException, IOException {

        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> metaParams = new HashMap<>();
        metaParams.put("bagSizePercent", bagSizePercent);
        metaParams.put("batchSize", batchSize);
        metaParams.put("numIterations", numIterations);
        params.put("datasetUri", datasetUri);
        params.put("minNumInstances", minNumInstances);
        params.put("unpruned", unpruned);
        params.put("useUnsmoothed", useUnsmoothed);
        params.put("buildRegressionTree", buildRegressionTree);
        return delegate.algorithmPost(fileInputStream, fileDetail, datasetUri, "M5P", params,
            "Bagging", metaParams, validation, validationNum, headers, ui, securityContext);
    }


    @POST
    @Path("/DecisionStump")
    @Consumes({ "multipart/form-data" })
    @Produces({ TEXT_URILIST, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "REST interface to the WEKA DecisionStump classifier.",
        notes = "REST interface to the DecisionStump classifier. " + SAVE_MODEL_NOTE, tags = {"algorithm"} )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Resource Not Found")})
    public Response algorithmDecisionStumpPost(
        //data params
        @FormDataParam("file") InputStream fileInputStream,
        @FormDataParam("file") FormDataContentDisposition fileDetail,
        @ApiParam(value = "Dataset URI or local dataset ID (to the arff representation of a dataset).") @FormDataParam("datasetURI") String datasetUri,
        // DecisionStump
        @ApiParam(value = "Validation to use.", allowableValues = "CrossValidation,Hold-Out", defaultValue = "CrossValidation") @FormDataParam("validation") String validation,
        @ApiParam(value = "Num of Crossvalidations or Percentage Split %.", defaultValue = "10") @FormDataParam("validationNum") Double validationNum,
        @ApiParam(value = "Authorization token" )@HeaderParam("subjectid") String subjectid,
        @Context UriInfo ui, @Context HttpHeaders headers, @Context SecurityContext securityContext)
        throws NotFoundException, IOException {

        HashMap<String, Object> params = new HashMap<>();
        params.put("datasetUri", datasetUri);
        return delegate.algorithmPost(fileInputStream, fileDetail, datasetUri, "DecisionStump", params,
            validation, validationNum, headers, ui, securityContext);
    }


    @POST
    @Path("/DecisionStump/adaboost")
    @Consumes({ "multipart/form-data" })
    @Produces({ TEXT_URILIST, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "REST interface to the WEKA AdaBoost M1 with DecisionStump classifier.",
        notes = "REST interface to the WEKA AdaBoost M1 with DecisionStump classifier. " + SAVE_MODEL_NOTE, tags = {"algorithm","meta algorithm"} )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Resource Not Found")})
    public Response algorithmDecisionStumpAdaBoostPost(
        //data params
        @FormDataParam("file") InputStream fileInputStream,
        @FormDataParam("file") FormDataContentDisposition fileDetail,
        @ApiParam(value = "Dataset URI or local dataset ID (to the arff representation of a dataset).") @FormDataParam("datasetURI") String datasetUri,
        //meta params,
        @ApiParam(value = "Adaboost M1: The preferred number of instances to process if batch prediction is being performed. More or fewer instances may be provided, but this gives implementations a chance to specify a preferred batch size.", defaultValue = "100") @FormDataParam("batchSize") Integer batchSize,
        @ApiParam(value = "Adaboost M1: The number of iterations to be performed.", defaultValue = "10") @FormDataParam("numIterations") Integer numIterations,
        @ApiParam(value = "Adaboost M1: Whether resampling is used instead of reweighting.", defaultValue = "0", allowableValues = "0, 1") @FormDataParam("useResampling") Integer useResampling,
        @ApiParam(value = "Adaboost M1: Weight threshold for weight pruning.", defaultValue = "100") @FormDataParam("weightThreshold") Integer weightThreshold,
        // DecisionStump
        @ApiParam(value = "Validation to use.", allowableValues = "CrossValidation,Hold-Out", defaultValue = "CrossValidation") @FormDataParam("validation") String validation,
        @ApiParam(value = "Num of Crossvalidations or Percentage Split %.", defaultValue = "10") @FormDataParam("validationNum") Double validationNum,
        @ApiParam(value = "Authorization token" )@HeaderParam("subjectid") String subjectid,
        @Context UriInfo ui, @Context HttpHeaders headers, @Context SecurityContext securityContext)
        throws NotFoundException, IOException {

        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> metaParams = new HashMap<>();
        params.put("datasetUri", datasetUri);
        metaParams.put("batchSize", batchSize);
        metaParams.put("numIterations", numIterations);
        metaParams.put("useResampling", useResampling);
        metaParams.put("weightThreshold", weightThreshold);
        return delegate.algorithmPost(fileInputStream, fileDetail, datasetUri, "DecisionStump", params,
            "AdaBoost", metaParams, validation, validationNum, headers, ui, securityContext);
    }


    @POST
    @Path("/DecisionStump/bagging")
    @Consumes({ "multipart/form-data" })
    @Produces({ TEXT_URILIST, MediaType.APPLICATION_JSON})
    @ApiOperation(value = "REST interface to the WEKA Bagging with DecisionStump classifier.",
        notes = "REST interface to the WEKA Bagging with DecisionStump classifier. " + SAVE_MODEL_NOTE, tags = {"algorithm","meta algorithm"} )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Resource Not Found")})
    public Response algorithmDecisionStumpBaggingPost(
        //data params
        @FormDataParam("file") InputStream fileInputStream,
        @FormDataParam("file") FormDataContentDisposition fileDetail,
        @ApiParam(value = "Dataset URI or local dataset ID (to the arff representation of a dataset).") @FormDataParam("datasetURI") String datasetUri,
        //meta params,
        @ApiParam(value = "Bagging: Size of each bag, as a percentage of the training set size.", defaultValue = "100") @FormDataParam("bagSizePercent") Integer bagSizePercent,
        @ApiParam(value = "Bagging: The preferred number of instances to process if batch prediction is being performed. More or fewer instances may be provided, but this gives implementations a chance to specify a preferred batch size.", defaultValue = "100") @FormDataParam("batchSize") Integer batchSize,
        @ApiParam(value = "Bagging: The number of iterations to be performed.", defaultValue = "10") @FormDataParam("numIterations") Integer numIterations,
        // DecisionStump
        @ApiParam(value = "Validation to use.", allowableValues = "CrossValidation,Hold-Out", defaultValue = "CrossValidation") @FormDataParam("validation") String validation,
        @ApiParam(value = "Num of Crossvalidations or Percentage Split %.", defaultValue = "10") @FormDataParam("validationNum") Double validationNum,
        @ApiParam(value = "Authorization token" )@HeaderParam("subjectid") String subjectid,
        @Context UriInfo ui, @Context HttpHeaders headers, @Context SecurityContext securityContext)
        throws NotFoundException, IOException {

        HashMap<String, Object> params = new HashMap<>();
        params.put("datasetUri", datasetUri);
        HashMap<String, Object> metaParams = new HashMap<>();
        metaParams.put("bagSizePercent", bagSizePercent);
        metaParams.put("batchSize", batchSize);
        metaParams.put("numIterations", numIterations);

        return delegate.algorithmPost(fileInputStream, fileDetail, datasetUri, "DecisionStump", params,
            "Bagging", metaParams, validation, validationNum, headers, ui, securityContext);
    }
}