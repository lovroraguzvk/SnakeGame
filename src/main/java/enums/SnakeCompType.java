package enums;

public enum SnakeCompType {
    HEAD_NORTH (Group.HEAD),
    HEAD_EAST (Group.HEAD),
    HEAD_SOUTH (Group.HEAD),
    HEAD_WEST (Group.HEAD),
    BODY_HORIZONTAL (Group.BODY),
    BODY_VERTICAL (Group.BODY),
    BODY_NE (Group.BODY),
    BODY_SE (Group.BODY),
    BODY_SW (Group.BODY),
    BODY_NW (Group.BODY),
    TAIL_NORTH (Group.TAIL),
    TAIL_EAST (Group.TAIL),
    TAIL_SOUTH (Group.TAIL),
    TAIL_WEST (Group.TAIL);

    private Group group;

    SnakeCompType(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public boolean isHead() {
        return group == Group.HEAD;
    }

    public boolean isBody() {
        return group == Group.BODY;
    }

    public boolean isTail() {
        return group == Group.TAIL;
    }

    private enum Group {
        HEAD,
        BODY,
        TAIL
    }
/*
    public String toString() {
        return switch (group) {
            case HEAD -> "Head";
            case BODY -> "Body";
            case TAIL -> "Tail";
            default -> "Unknown";
        };
    }

 */

}
