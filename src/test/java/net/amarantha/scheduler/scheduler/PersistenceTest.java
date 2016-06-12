package net.amarantha.scheduler.scheduler;

import com.google.inject.Inject;
import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.scheduler.TestModule;
import net.amarantha.scheduler.cue.Cue;
import net.amarantha.scheduler.cue.CueFactory;
import net.amarantha.scheduler.exception.DuplicateCueException;
import net.amarantha.scheduler.http.Param;
import org.glassfish.grizzly.http.Method;
import org.junit.Assert;
import org.junit.runner.RunWith;

import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.List;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class PersistenceTest {

    @Inject private CueFactory cueFactory;
    @Inject private JsonEncoder encoder;
    @Inject private Scheduler scheduler;

    private List<Cue> testCues = new ArrayList<>();

    private Cue midiCue1;
    private Cue midiCue2;
    private Cue httpCue1;
    private Cue httpCue2;

    @Story
    public void testCuesJson() {

        given_cues();

        then_there_are_$1_cues(testCues.size());

        scheduler.saveCues();

        scheduler.getCues().clear();

        then_there_are_$1_cues(0);

        scheduler.loadCues();

        then_there_are_$1_cues(testCues.size());

        then_cues_are_correct();

    }

    void given_cues() {
        try {
            scheduler.addCue(midiCue1 = cueFactory.makeMidiCue(1, "Midi1", ShortMessage.NOTE_ON, 1, 64, 127));
            scheduler.addCue(midiCue2 = cueFactory.makeMidiCue(2, "Midi2", ShortMessage.NOTE_OFF, 2, 62, 64));
            scheduler.addCue(httpCue1 = cueFactory.makeHttpCue(3, "Http1", Method.POST, "localhost:8001", "test", "stuff", new Param("thing", "wotsit")));
            scheduler.addCue(httpCue2 = cueFactory.makeHttpCue(4, "Http2", Method.GET, "localhost:8001", "mock", null));
            testCues.add(midiCue1);
            testCues.add(midiCue2);
            testCues.add(httpCue1);
            testCues.add(httpCue2);
        } catch (DuplicateCueException e) {
            e.printStackTrace();
        }
    }

    void then_cues_are_correct() {
        for ( Cue cue : testCues ) {
            Assert.assertEquals(cue, scheduler.getCue(cue.getId()));
        }
    }

    void then_there_are_$1_cues(int count) {
        Assert.assertEquals(count, scheduler.getCues().size());
    }


}
