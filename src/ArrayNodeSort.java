		ArrayNode books = JsonNodeFactory.instance.arrayNode();	//정렬이 필요한 ArrayNode
		
		ObjectNode effectiveJava = books.addObject();
		effectiveJava.put("name", "이펙티브 자바 2판");
		effectiveJava.put("seq", 5);
		
		ObjectNode tobySpring = books.addObject();
		tobySpring.put("title", "토비의 스프링 3");
		tobySpring.put("seq", 7);
		
		ObjectNode swift3 = books.addObject();
		swift3.put("name", "꼼꼼한 재은씨의 스위프트3");
		swift3.put("seq", 1);
		
		ArrayNode addArr = books.addArray();
		addArr.add("cat");
		addArr.add("dog");
		
		books.add("그냥 텍스트..");
		
		System.out.println("books : " + books);
		
		
		ArrayNode resultNode = JsonNodeFactory.instance.arrayNode();	//정렬된 결과 ArrayNode
		List<ObjectNode> objNodeList = new ArrayList<ObjectNode>();		//정렬이 필요한 ObjectNode List
		
		for(JsonNode node : books){
			if(node.isObject()){
				//ObjectNode인지 판단해 리스트에 저장
				objNodeList.add((ObjectNode)node);
			} else {
				//ObjectNode 아닌경우 결과노드에 바로 저장
				resultNode.add(node);
			}
		}
		
		Collections.sort(objNodeList, new Comparator<ObjectNode>(){

			public int compare(ObjectNode o1, ObjectNode o2) {
				return o1.path("seq").asInt() - o2.path("seq").asInt();	//seq를 사용해 정렬
			}
			
		});
		
		String jsonStr = JsonUtil.toJson(objNodeList);
		ArrayNode sortedBooks = JsonUtil.toObject(jsonStr, ArrayNode.class);
		
		System.out.println("sortedNode : " + sortedBooks);
		
		resultNode.addAll(sortedBooks);	//정렬한 ArrayNode add
		
		System.out.println("result : " + resultNode);