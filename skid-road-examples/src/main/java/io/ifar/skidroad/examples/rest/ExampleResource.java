package io.ifar.skidroad.examples.rest;

import com.google.common.base.Function;
import io.ifar.goodies.Triple;
import io.ifar.skidroad.jersey.single.IDTagTripleTransformFactory;
import io.ifar.skidroad.jersey.single.RecorderFactory;
import io.ifar.skidroad.jersey.single.RequestTimestampFilter;
import io.ifar.skidroad.recorder.Recorder;
import io.ifar.skidroad.writing.WritingWorkerManager;

import javax.ws.rs.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Post to this resource with:
 * curl -X POST -H "Content-Type: application/json" -d '{"radius":1.2, "with_gold":true}' http://localhost:8080/orders
 *
 * Request and response will be logged because of applied container filters. Alternate approach would be for this
 * resource (or a collaborator) to have a direct reference to a Skid Road WritingWorkerManager and explicitly pass
 * data into it.
 */
@Path("/")
public class ExampleResource {
    private final WritingWorkerManager<Triple<String,String,String>> writingWorkerManager;
    private final Function<String,Triple<String,String,String>> favColorCapture = IDTagTripleTransformFactory.passThrough("FAV_COLOR");

    public ExampleResource(WritingWorkerManager<Triple<String, String, String>> writingWorkerManager) {
        this.writingWorkerManager = writingWorkerManager;
    }

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @POST
    @Path("orders")
    @Consumes("application/json")
    @Produces("application/json")
    public RainbowRequestResponse requestRainbow(RainbowRequest req, @QueryParam("fav_color") String favoriteColor) {
        String rainbowColor;
        if (favoriteColor == null) {
            rainbowColor = "all";
        } else {
            rainbowColor = favoriteColor;
        }
        writingWorkerManager.record(
                RequestTimestampFilter.getRequestDateTime(),
                favColorCapture.apply(favoriteColor)
        );

        //demo use of Recorder to allow parts of code with no reference to request
        // or writer to easily record entries.
        final Recorder<String> recorder = RecorderFactory.<String>build(
                "LATER",
                writingWorkerManager
        );
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
                recorder.record(String.format("The future has arrived on thread \"%s\".", Thread.currentThread().getName()));
            }
        });

        return new RainbowRequestResponse(true,rainbowColor);
    }
}
