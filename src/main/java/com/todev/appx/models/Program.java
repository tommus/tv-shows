package com.todev.appx.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;
import com.todev.appx.serializers.DateTimeDeserializer;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class Program {
  @GeneratedValue
  @Id
  @JsonIgnore
  private long id;

  @JsonDeserialize(using = DateTimeDeserializer.class)
  @JsonProperty(value = "start_time")
  @JsonSerialize(using = DateTimeSerializer.class)
  @JsonView({ View.Schedule.class, Station.View.Details.class })
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime startAt;

  @JsonIgnore
  @ManyToOne(cascade = CascadeType.DETACH)
  private Station station;

  @JsonIgnore
  @ManyToOne(cascade = CascadeType.DETACH)
  private Show show;

  @JsonProperty(value = "time_passed")
  @JsonView(View.Details.class)
  @Transient
  private long sinceStart;

  @JsonProperty(value = "time_left")
  @JsonView(View.Details.class)
  @Transient
  private long tillEnd;

  public Program() {
    // Default constructor for Hibernate.
  }

  public Program(Station station, Show show, DateTime startAt) {
    this.station = station;
    this.show = show;
    this.startAt = startAt;
  }

  public long getId() {
    return id;
  }

  @JsonView({ View.Basic.class, Station.View.Details.class })
  public String getName() {
    return show.getName();
  }

  @JsonView({ View.Basic.class, Station.View.Details.class })
  public String getBrief() {
    return show.getBrief();
  }

  public DateTime getStartAt() {
    return startAt;
  }

  public void setStartAt(DateTime startAt) {
    this.startAt = startAt;
  }

  @JsonView(View.Invisible.class)
  public int getDuration() {
    return show.getDuration();
  }

  public Station getStation() {
    return station;
  }

  public void setStation(Station station) {
    this.station = station;
  }

  public Show getShow() {
    return show;
  }

  public void setShow(Show show) {
    this.show = show;
  }

  public long getSinceStart() {
    return sinceStart;
  }

  public void setSinceStart(long sinceStart) {
    this.sinceStart = sinceStart;
  }

  public long getTillEnd() {
    return tillEnd;
  }

  public void setTillEnd(long tillEnd) {
    this.tillEnd = tillEnd;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getStartAt(), getStation(), getShow(), getSinceStart(), getTillEnd());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (obj instanceof Program) {
      final Program other = (Program) obj;

      return Objects.equals(getId(), other.getId())
          && Objects.equals(getStartAt(), other.getStartAt())
          && Objects.equals(getStation(), other.getStation())
          && Objects.equals(getShow(), other.getShow())
          && Objects.equals(getSinceStart(), other.getSinceStart())
          && Objects.equals(getTillEnd(), other.getTillEnd());
    }

    return false;
  }

  @Override
  public String toString() {
    return "Program {id = "
        + Objects.toString(getId())
        + ", start_at = "
        + Objects.toString(getStartAt().toString())
        + ", station = "
        + Objects.toString(getStation())
        + ", show = "
        + Objects.toString(getShow())
        + ", time_passed = "
        + Objects.toString(getSinceStart())
        + ", time_left = "
        + Objects.toString(getTillEnd())
        + "}";
  }

  /**
   * Class allow to define which fields will be present in queried response.
   */
  public static class View {
    public static class Invisible {
    }

    public static class Basic {
    }

    public static class Details extends Basic {
    }

    public static class Schedule extends Basic {
    }
  }
}
