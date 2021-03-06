package hongjiancheng.tojava;

import org.junit.Test;
import static junit.framework.TestCase.assertEquals;


public class ParserTest {

    @Test
    public void getCommandWord() {
        assertEquals("todo", Parser.getCommandWord("todo lol"));
        assertEquals("deadline", Parser.getCommandWord("deadline dada"));
        assertEquals("print", Parser.getCommandWord("print"));
        assertEquals("", Parser.getCommandWord(""));
    }

    @Test
    public void createTodo() throws TaskManagerException {
        ToDo actual = Parser.createTodo("todo read book");
        ToDo expected= new ToDo("read book");
        assertEquals(expected.print(), actual.print());
    }

    @Test
    public void createDeadline() throws TaskManagerException {
        Deadline actual = Parser.createDeadline("deadline read book /by 2018-12-31_00:00:00");
        Deadline expected = new Deadline("read book", Parser.convertDeadline("2018-12-31_00:00:00"));
        assertEquals(expected.print(), actual.print());
    }
}