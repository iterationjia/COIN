package com.ac.coin.util;

import com.ac.coin.enums.NodeShape;
import com.ac.coin.enums.Size;
import com.ac.coin.po.*;
import com.ac.coin.vo.*;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.value.*;
import org.neo4j.driver.types.Relationship;

import java.util.HashMap;
import java.util.Map;

public class Transform {
    public static Relation relationshipValueToRelation(RelationshipValue relationshipValue){
        Relation relation = new Relation();
        Relationship relationship = relationshipValue.asRelationship();

        relation.setId(relationship.id());
        relation.setFromId(relationship.startNodeId());
        relation.setToId(relationship.endNodeId());

        Map<String, Object> properties = relationship.asMap();
        relation.setGraphId((Long)properties.get("graphId"));
        relation.setName((String)properties.get("name"));
        relation.setLabel((String)properties.get("label"));
        relation.setShown(Boolean.parseBoolean((String)properties.get("isShown")));
        relation.setSolid(Boolean.parseBoolean((String)properties.get("isSolid")));
        relation.setHighlighted(Boolean.parseBoolean((String)properties.get("isHighLighted")));
        return relation;
    }

    public static MapValue relationToMapValue(Relation relation){
        Map<String, Value> properties = new HashMap<>();

        properties.put("graphId",new IntegerValue(relation.getGraphId()));
        properties.put("name",new StringValue(relation.getName()));
        if(relation.getLabel()!=null) properties.put("label",new StringValue(relation.getLabel()));
        else properties.put("label",new StringValue(""));
        properties.put("isShown", new StringValue(String.valueOf(relation.isShown())));
        properties.put("isSolid", new StringValue(String.valueOf(relation.isSolid())));
        properties.put("isHighLighted", new StringValue(String.valueOf(relation.isHighlighted())));
        return new MapValue(properties);
    }

    public static RelationVO relationVO(Relation relation){
        RelationVO relationVO = new RelationVO();

        relationVO.setId(relation.getId());
        relationVO.setSource(relation.getFromId());
        relationVO.setTarget(relation.getToId());
        relationVO.setGraphId(relation.getGraphId());
        relationVO.setName(relation.getName());
        relationVO.setLabel(relation.getLabel());
        relationVO.setSolid(relation.isSolid());
        relationVO.setHighlighted(relation.isHighlighted());
        relationVO.setShown(relation.isShown());
        return relationVO;
    }

    public static Relation relationPO(RelationVO relationVO) {
        Relation relation = new Relation();

        relation.setGraphId(relationVO.getGraphId());
        relation.setName(relationVO.getName());
        relation.setLabel(relationVO.getLabel());

        relation.setFromId(relationVO.getSource());
        relation.setToId(relationVO.getTarget());
        relation.setSolid(relationVO.isSolid());
        relation.setHighlighted(relationVO.isHighlighted());
        relation.setShown(relationVO.isShown());

        if (relationVO.getId() != -1) relation.setId(relationVO.getId());

        return relation;
    }

    public static NodeVO nodeVO(Node node){
        NodeVO nodeVO = new NodeVO();
        String className = node.getClass().getName();

        switch (className.substring(className.lastIndexOf('.')+1)) {
            case "Company":
                CompanyVO companyVO = new CompanyVO();

                Company company = (Company) node;
                companyVO.setCompany_id(company.getCompany_id());
                companyVO.setRegister_location(company.getRegister_location());
                companyVO.setRegister_capital(company.getRegister_capital());
                companyVO.setEstablish_date(company.getEstablish_date());

                companyVO.setEmployee_num(company.getEmployee_num());
                companyVO.setRetire_rate(company.getRetire_rate());
                companyVO.setAverage_education(company.getAverage_education());

                nodeVO = companyVO;
                break;
            case "Person":
                PersonVO personVO = new PersonVO();

                Person person = (Person) node;
                personVO.setTitle_class(person.getTitle_class());
                personVO.setTitle_level(person.getTitle_level());
                personVO.setTitle(person.getTitle());
                personVO.setBirth_year(person.getBirth_year());
                personVO.setGender(person.getGender());
                personVO.setDegree(person.getDegree());
                personVO.setResume(person.getResume());

                nodeVO = personVO;
                break;
            case "FirstIndustry":
                nodeVO = new FirstIndustryVO();
                break;
            case "SecondIndusty":
                nodeVO = new SecondIndustryVO();
                break;
            case "Stock":
                StockVO stockVO = new StockVO();
                Stock stock = (Stock) node;
                stockVO.setStock_code(stock.getStock_code());
                stockVO.setStart_date(stock.getStart_date());
                stockVO.setSt_rate(stock.getSt_rate());

                nodeVO = stockVO;
                break;
            case "Price":
                PriceVO priceVO = new PriceVO();
                Price price = (Price) node;
                priceVO.setDate(price.getDate());
                priceVO.setOpen(price.getOpen());
                priceVO.setClose(price.getClose());
                priceVO.setHigh(price.getHigh());
                priceVO.setLow(price.getLow());
                priceVO.setVolume(price.getVolume());
                priceVO.setMoney(price.getMoney());

                nodeVO = priceVO;
                break;
            case "PriceStatisticVO":
                PriceStatisticVO priceStatisticVO = new PriceStatisticVO();
                PriceStatistic priceStatistic = (PriceStatistic) node;
                priceStatisticVO.set_7_expected_price(priceStatistic.get_7_expected_price());
                priceStatisticVO.set_7_minus_price(priceStatistic.get_7_minus_price());
                priceStatisticVO.set_7_risk_price(priceStatistic.get_7_risk_price());
                priceStatisticVO.set_7_pic_url(priceStatistic.get_7_pic_url());
                priceStatisticVO.set_15_expected_price(priceStatistic.get_15_expected_price());
                priceStatisticVO.set_15_minus_price(priceStatistic.get_15_minus_price());
                priceStatisticVO.set_15_risk_price(priceStatistic.get_15_risk_price());
                priceStatisticVO.set_15_pic_url(priceStatistic.get_15_pic_url());
                priceStatisticVO.set_30_expected_price(priceStatistic.get_30_expected_price());
                priceStatisticVO.set_30_minus_price(priceStatistic.get_30_minus_price());
                priceStatisticVO.set_30_risk_price(priceStatistic.get_30_risk_price());
                priceStatisticVO.set_30_pic_url(priceStatistic.get_30_pic_url());

                nodeVO = priceStatisticVO;
                break;
            default:
                break;
        }
        nodeVO.setId(node.getId());
        nodeVO.setGraphId(node.getGraphId());
        nodeVO.setName(node.getName());
        nodeVO.setLabel(node.getLabel());
        nodeVO.setColor(node.getColor());
        if (node.getShape()==null) node.setShape(NodeShape.CIRCLE);
        nodeVO.setShape(node.getShape().toString());
        nodeVO.setX(node.getX());
        nodeVO.setY(node.getY());
        nodeVO.setNode_size(node.getNode_size());
        nodeVO.setFont_size(node.getFont_size());
        nodeVO.setHighlighted(node.isHighlighted());
        nodeVO.setTypesetting_x(node.getTypesetting_x());
        nodeVO.setTypesetting_y(node.getTypesetting_y());
        nodeVO.setShown(node.isShown());

        return nodeVO;
    }

    public static Node nodePO(NodeVO nodeVO){
        Node node = new Node();
        String className = nodeVO.getClass().getName();
        switch (className.substring(className.lastIndexOf('.')+1)) {
            case "CompanyVO":
                CompanyVO companyVO = (CompanyVO) nodeVO;

                Company company = new Company(companyVO.getCompany_id());
                company.setRegister_location(companyVO.getRegister_location());
                company.setRegister_capital(companyVO.getRegister_capital());
                company.setEstablish_date(companyVO.getEstablish_date());

                company.setEmployee_num(companyVO.getEmployee_num());
                company.setRetire_rate(companyVO.getRetire_rate());
                company.setAverage_education(companyVO.getAverage_education());

                node = company;
                break;
            case "PersonVO":
                PersonVO personVO = (PersonVO) nodeVO;
                Person person = new Person();

                person.setTitle_class(personVO.getTitle_class());
                person.setTitle_level(personVO.getTitle_level());
                person.setTitle(personVO.getTitle());
                person.setBirth_year(personVO.getBirth_year());
                person.setGender(personVO.getGender());
                person.setDegree(personVO.getDegree());
                person.setResume(personVO.getResume());

                node = person;
                break;
            case "FirstIndustryVO":
                node = new FirstIndustry();
                break;
            case "SecondIndustryVO":
                node = new SecondIndustry();
                break;
            case "StockVO":
                StockVO stockVO = (StockVO) nodeVO;
                Stock stock = new Stock();
                stock.setStock_code(stockVO.getStock_code());
                stock.setStart_date(stockVO.getStart_date());
                stock.setSt_rate(stockVO.getSt_rate());

                node = stock;
                break;
            case "PriceVO":
                PriceVO priceVO = (PriceVO) nodeVO;
                Price price = new Price();
                price.setDate(priceVO.getDate());
                price.setOpen(priceVO.getOpen());
                price.setClose(priceVO.getClose());
                price.setHigh(priceVO.getHigh());
                price.setLow(priceVO.getLow());
                price.setVolume(priceVO.getVolume());
                price.setMoney(priceVO.getMoney());

                node = price;
                break;
            case "PriceStatisticVO":
                PriceStatistic priceStatistic = new PriceStatistic();
                PriceStatisticVO priceStatisticVO = (PriceStatisticVO) nodeVO;

                priceStatistic.set_7_expected_price(priceStatisticVO.get_7_expected_price());
                priceStatistic.set_7_minus_price(priceStatisticVO.get_7_minus_price());
                priceStatistic.set_7_risk_price(priceStatisticVO.get_7_risk_price());
                priceStatistic.set_7_pic_url(priceStatisticVO.get_7_pic_url());
                priceStatistic.set_15_expected_price(priceStatisticVO.get_15_expected_price());
                priceStatistic.set_15_minus_price(priceStatisticVO.get_15_minus_price());
                priceStatistic.set_15_risk_price(priceStatisticVO.get_15_risk_price());
                priceStatistic.set_15_pic_url(priceStatisticVO.get_15_pic_url());
                priceStatistic.set_30_expected_price(priceStatisticVO.get_30_expected_price());
                priceStatistic.set_30_minus_price(priceStatisticVO.get_30_minus_price());
                priceStatistic.set_30_risk_price(priceStatisticVO.get_30_risk_price());
                priceStatistic.set_30_pic_url(priceStatisticVO.get_30_pic_url());

                node = priceStatistic;
                break;
            default:
                break;
        }

        //必填
        node.setName(nodeVO.getName());
        node.setLabel(nodeVO.getLabel());
        node.setGraphId(nodeVO.getGraphId());
        node.setX(nodeVO.getX());
        node.setY(nodeVO.getY());

        //可选
        node.setShape((nodeVO.getShape()!=null)? NodeShape.valueOf(nodeVO.getShape().toUpperCase()): NodeShape.CIRCLE);
        node.setColor((nodeVO.getColor()!=null)?nodeVO.getColor():"#58c5c7");
        node.setNode_size((nodeVO.getNode_size()!=-1)?nodeVO.getNode_size(): Size.DEFAULT_NODE_SIZE.getValue());
        node.setFont_size((nodeVO.getFont_size()!=-1)?nodeVO.getFont_size():Size.DEFAULT_FONT_SIZE.getValue());
        node.setTypesetting_x(nodeVO.getTypesetting_x());
        node.setTypesetting_y(nodeVO.getTypesetting_y());
        if(nodeVO.getId()!=-1) node.setId(nodeVO.getId());
        node.setShown(nodeVO.isShown());
        return node;
    }

    public static GraphVO graphVO(Graph graph){
        GraphVO graphVO = new GraphVO();
        graphVO.setId(graph.getId());
        graphVO.setName(graph.getName());
        graphVO.setNodeLabelVisible(graph.isNodeLabelVisible());
        graphVO.setRelationLabelVisible(graph.isRelationLabelVisible());
        graphVO.setUrl(graph.getUrl());
        graphVO.setTime(graph.getTime());
        graphVO.setFavored(graph.getFavored());
        return graphVO;
    }

    public static Graph graphPO(GraphVO graphVO){
        Graph graph = new Graph(graphVO.getName());
        if(graphVO.getId()!=-1) graph.setId(graphVO.getId());
        graph.setNodeLabelVisible(graphVO.isNodeLabelVisible());
        graph.setRelationLabelVisible(graphVO.isRelationLabelVisible());
        graph.setUrl(graphVO.getUrl());
        graph.setTime(graphVO.getTime());
        graph.setFavored(graphVO.getFavored());
        return graph;
    }

    public static UserVO userVO(User user){
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setName(user.getName());
        userVO.setPassword(user.getPassword());
        userVO.setBalance(user.getBalance());
        userVO.setRisk(user.getRisk());
        userVO.setAcceptSt(user.isAcceptSt());
        userVO.setStocks(user.getStocks());
        userVO.setIndustries(user.getIndustries());
        return userVO;
    }

    public static User userPO(UserVO userVO){
        User user = new User(userVO.getName(),userVO.getPassword());
        if(userVO.getId()!=-1) user.setId(userVO.getId());
        user.setRisk((userVO.getRisk()!=-1)? userVO.getRisk():1);
        user.setBalance((userVO.getBalance()!=-1)? userVO.getBalance():2.0);
        user.setStocks(userVO.getStocks());
        user.setIndustries(userVO.getIndustries());
        user.setAcceptSt(userVO.isAcceptSt());
        return user;
    }
}
