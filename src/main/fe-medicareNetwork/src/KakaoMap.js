import React, { useState, useEffect, useRef } from 'react';
import Session from "react-session-api/src";
import './css/KakaoMap.css'; // CSS 파일을 불러옵니다
import axios from "axios";

const {kakao} = window;

const MapComponent = () => {
    const [keyword, setKeyword] = useState(' 약국');
    const member = JSON.parse(window.sessionStorage.getItem("member1"));
    const mapRef = useRef(null);
    const markers = useRef([]);
    const infowindow = useRef(null);
    const ps = useRef(null);
    const map = useRef(null);

    useEffect(() => {
        setKeyword(member.memberAddress + " 약국");
        const initializeMap = () => {
            const mapContainer = mapRef.current;
            const mapOption = {
                center: new kakao.maps.LatLng(37.566826, 126.9786567),
                level: 3,
            };

            map.current = new kakao.maps.Map(mapContainer, mapOption);
            infowindow.current = new kakao.maps.InfoWindow({ zIndex: 1 });
            ps.current = new kakao.maps.services.Places();

            // 기본 검색
            searchPlaces();
        };

        if (!window.kakao) {
            const script = document.createElement('script');
            script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=4b8c4b3b040c75339ac71ef15a23de49&libraries=services`;
            script.async = true;
            script.onload = initializeMap;
            document.head.appendChild(script);
        } else {
            initializeMap();
        }
    }, []);

    const searchPlaces = () => {
        if (!keyword.trim()) {
            alert('키워드를 입력해주세요!');
            return;
        }

        ps.current.keywordSearch(keyword, (data, status, pagination) => {
            if (status === kakao.maps.services.Status.OK) {
                displayPlaces(data);
                displayPagination(pagination);
            } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
                alert('검색 결과가 존재하지 않습니다.');
            } else if (status === kakao.maps.services.Status.ERROR) {
                alert('검색 결과 중 오류가 발생했습니다.');
            }
        });
    };

    const displayPlaces = (places) => {
        const listEl = document.getElementById('placesList');
        const menuEl = document.getElementById('menu_wrap');
        const fragment = document.createDocumentFragment();
        const bounds = new kakao.maps.LatLngBounds();

        removeAllChildNodes(listEl);
        removeMarkers();

        places.forEach((place, index) => {
            const placePosition = new kakao.maps.LatLng(place.y, place.x);
            const marker = addMarker(placePosition, index);
            const itemEl = getListItem(index, place);

            bounds.extend(placePosition);

            (function (marker, title) {
                kakao.maps.event.addListener(marker, 'mouseover', () => {
                    displayInfowindow(marker, title);
                });
                kakao.maps.event.addListener(marker, 'mouseout', () => {
                    infowindow.current.close();
                });
                itemEl.onmouseover = () => {
                    displayInfowindow(marker, title);
                };
                itemEl.onmouseout = () => {
                    infowindow.current.close();
                };
            })(marker, place.place_name);

            fragment.appendChild(itemEl);
        });

        listEl.appendChild(fragment);
        menuEl.scrollTop = 0;
        map.current.setBounds(bounds); // Use map.current instead of mapRef.current.getMap()
    };

    const getListItem = (index, place) => {
        const el = document.createElement('li');
        let itemStr = `
            <span class="markerbg marker_${index + 1}"></span>
            <div class="info">
                <h5>${place.place_name}</h5>
                ${place.road_address_name ? `
                    <span>${place.road_address_name}</span>
                    <span class="jibun gray">${place.address_name}</span>
                ` : `
                    <span>${place.address_name}</span>
                `}
                <span class="tel">${place.phone}</span>
            </div>
        `;
        el.innerHTML = itemStr;
        el.className = 'item';
        return el;
    };

    const addMarker = (position, idx) => {
        const imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png';
        const imageSize = new kakao.maps.Size(36, 37);
        const imgOptions = {
            spriteSize: new kakao.maps.Size(36, 691),
            spriteOrigin: new kakao.maps.Point(0, (idx * 46) + 10),
            offset: new kakao.maps.Point(13, 37),
        };
        const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions);
        const marker = new kakao.maps.Marker({
            position,
            image: markerImage,
        });

        marker.setMap(map.current);
        markers.current.push(marker);

        return marker;
    };

    const removeMarkers = () => {
        markers.current.forEach(marker => marker.setMap(null));
        markers.current = [];
    };

    const displayPagination = (pagination) => {
        const paginationEl = document.getElementById('pagination');
        const fragment = document.createDocumentFragment();

        while (paginationEl.hasChildNodes()) {
            paginationEl.removeChild(paginationEl.lastChild);
        }

        for (let i = 1; i <= pagination.last; i++) {
            const el = document.createElement('a');
            el.href = '#';
            el.innerHTML = i;

            if (i === pagination.current) {
                el.className = 'on';
            } else {
                el.onclick = (function (i) {
                    return function () {
                        pagination.gotoPage(i);
                    };
                })(i);
            }

            fragment.appendChild(el);
        }

        paginationEl.appendChild(fragment);
    };

    const displayInfowindow = (marker, title) => {
        const content = `<div style="padding:5px;z-index:1;">${title}</div>`;
        infowindow.current.setContent(content);
        infowindow.current.open(map.current, marker);
    };

    const removeAllChildNodes = (el) => {
        while (el.hasChildNodes()) {
            el.removeChild(el.lastChild);
        }
    };

    const handleSearch = (event) => {
        event.preventDefault();
        searchPlaces();
    };

    return (
        <div className="map_wrap">
            <div id="map" ref={mapRef} style={{ width: '100%', height: '500px', position: 'relative', overflow: 'hidden' }}></div>
            <div id="menu_wrap" className="bg_white">
                <div className="option">
                    <form onSubmit={handleSearch}>
                        키워드 :
                        <input
                            type="text"
                            value={keyword}
                            onChange={(e) => setKeyword(e.target.value)}
                            size="15"
                        />
                        <button type="submit">검색하기</button>
                    </form>
                </div>
                <hr />
                <ul id="placesList"></ul>
                <div id="pagination"></div>
            </div>
        </div>
    );
};

export default MapComponent;
