package org.tools4j.fixgrep.orders

import org.tools4j.fix.Field
import org.tools4j.fix.FieldImpl
import org.tools4j.fix.FieldsImpl
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: benjw
 * Date: 01/10/2018
 * Time: 17:15
 */
class UniqueIdTest extends Specification {
    private final static int msgTypeTagNum = 35
    private final static int clOrdIdTagNum = 11
    private final static int origClOrdIdTagNum = 41
    private final static int senderCompIdTagNum = 49
    private final static int targetCompIdTagNum = 56
    private final static int orderIdTagNum = 37
    private final static int execTypeTagNum = 150

    @Unroll
    def "Equals - orderId with just sender and target ids id1:#id1 id2:#id2 expectEquals:#expectEquals"(AbstractUniqueId id1, AbstractUniqueId id2, boolean expectEquals, boolean hashCodeEquals) {
        expect:
        if(expectEquals){
            assert id1 == id2
        } else {
            assert id1 != id2
        }
        if(hashCodeEquals){
            assert id1.hashCode() == id2.hashCode()
        } else {
            assert id1.hashCode() != id2.hashCode() //hoping for no 'unexpected' hash collisions
        }

        where:
        id1                                                    | id2                                                          | expectEquals | hashCodeEquals
        clOrdId("ABC", "C", "S")  | clOrdId("ABC", "C", "S")        | true         | true
        clOrdId("ABC", "C", "S")  | origClOrdId("ABC", "C", "S")    | true         | true
        clOrdId("ABC", "C", "S")  | clOrdId("ABC", "Blah", "S")     | false        | false
        clOrdId("ABC", "C", "S")  | clOrdId("ABC", "S", "C")        | true         | true
        clOrdId("ABC", "C", "S")  | origClOrdId("ABC", "S", "C")    | true         | true
        clOrdId("ABC", "C", "S")  | clOrdId("ABC", "S", "Blah")     | false        | false
        clOrdId("ABC", null, "S") | clOrdId("ABC", null, "S")       | true         | true
        clOrdId("ABC", null, "S") | origClOrdId("ABC", null, "S")   | true         | true
        clOrdId("ABC", null, "S") | clOrdId("ABC", "S", null)       | true         | true
        clOrdId("ABC", null, "S") | origClOrdId("ABC", "S", null)   | true         | true
        clOrdId("ABC", null, "S") | clOrdId("ABC", "Blah", null)    | false        | false
        orderId("ABC", "C", "S")  | clOrdId("ABC", "C", "S")        | false        | true //hash collision, but ok
        orderId("ABC", "C", "S")  | origClOrdId("ABC", "C", "S")    | false        | true //hash collision, but ok
    }

    @Unroll
    def "orderId and (clOrdId or origClOrdId) should not be equal id1:#id1 id2:#id2 expectEquals:#expectEquals"(AbstractUniqueId id1, AbstractUniqueId id2, boolean expectEquals) {
        expect:
        if(expectEquals){
            assert id1 == id2
        } else {
            assert id1 != id2
            //hashcodes might be equal.  But that's ok
        }

        where:
        id1                                                    | id2                                                           | expectEquals
        orderId("ABC", "C", "S")  | orderId("ABC", "C", "S")         | true
        orderId("ABC", "C", "S")  | clOrdId("ABC", "C", "S")         | false
    }

    @Unroll
    def "Equals - orderId with other fields ids id1:#id1 id2:#id2 expectEquals:#expectEquals"(AbstractUniqueId id1, AbstractUniqueId id2, boolean expectEquals, boolean hashCodeEquals) {
        expect:
        if(expectEquals){
            assert id1 == id2
        } else {
            assert id1 != id2
        }
        if(hashCodeEquals){
            assert id1.hashCode() == id2.hashCode()
        } else {
            assert id1.hashCode() != id2.hashCode() //hoping for no unexpected hash collisions
        }

        where:
        id1 | id2 | expectEquals | hashCodeEquals
        orderId("ABC", "C", "S", f(1, "one"))  | orderId("ABC", "C", "S", f(1, "one")) | true | true
        orderId("ABC", "C", "S", f(1, "one"))  | orderId("ABC", "C", "S", f(1, "BLAH")) | false | false
        orderId("ABC", "C", "S", f(1, "one"), f(2, "two"))  | orderId("ABC", "C", "S", f(1, "one"), f(2, "two")) | true | true
        orderId("ABC", "C", "S", f(1, "one"), f(2, "two"))  | orderId("ABC", "C", "S", f(2, "two"), f(1, "one")) | true | true
        orderId("ABC", "C", "S", f(1, "one"), f(2, "two"))  | orderId("ABC", "C", "S", f(1, "two"), f(2, "one")) | false | true
        orderId("ABC", "C", "S", f(1, "one"), f(2, "two"))  | orderId("ABC", "C", "S", f(1, "one")) | false | false
    }

    UniqueOrderId orderId(String id, String senderCompId, String targetCompId, Field ... otherFields){
        return new UniqueOrderId(
                id == null ? null: new FieldImpl(orderIdTagNum, id),
                senderCompId == null ? null: new FieldImpl(senderCompIdTagNum, senderCompId),
                targetCompId == null ? null: new FieldImpl(targetCompIdTagNum, targetCompId),
                new FieldsImpl(Arrays.asList(otherFields)))
    }

    UniqueClientOrderId clOrdId(String id, String senderCompId, String targetCompId, Field ... otherFields){
        return new UniqueClientOrderId(
                id == null ? null: new FieldImpl(clOrdIdTagNum, id),
                senderCompId == null ? null: new FieldImpl(senderCompIdTagNum, senderCompId),
                targetCompId == null ? null: new FieldImpl(targetCompIdTagNum, targetCompId),
                new FieldsImpl(Arrays.asList(otherFields)))
    }

    UniqueClientOrderId origClOrdId(String id, String senderCompId, String targetCompId, Field ... otherFields){
        return new UniqueClientOrderId(
                id == null ? null: new FieldImpl(origClOrdIdTagNum, id),
                senderCompId == null ? null: new FieldImpl(senderCompIdTagNum, senderCompId),
                targetCompId == null ? null: new FieldImpl(targetCompIdTagNum, targetCompId),
                new FieldsImpl(Arrays.asList(otherFields)))
    }

    Field f (int tagNumber, String value){
        return new FieldImpl(tagNumber, value)
    }
}
