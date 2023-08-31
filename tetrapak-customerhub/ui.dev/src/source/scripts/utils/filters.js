import { storageUtil } from '../common/common';
import {EQ_TYPE,EQ_FILTERS,EQ_LOCAL_STORAGE,TPCOOKIEUSERTEM} from './constants';

export default {
  setFilterChipInLocalStorage(type,filterObject,countryCode){
    const filterChipsStorageObj={};
    if(type===EQ_TYPE){
      filterChipsStorageObj[EQ_FILTERS.COUNTRY] = countryCode;
      filterChipsStorageObj[EQ_FILTERS.EQUIPMENTNAME] = filterObject[EQ_FILTERS.EQUIPMENTNAME];
      filterChipsStorageObj[EQ_FILTERS.SERIALNUMBER] = filterObject[EQ_FILTERS.SERIALNUMBER];
      if(storageUtil.getCookie(TPCOOKIEUSERTEM)){
        filterChipsStorageObj[EQ_FILTERS.USERNAME]= storageUtil.getCookie(TPCOOKIEUSERTEM);
      }
      else{
        filterChipsStorageObj[EQ_FILTERS.USERNAME]= '41d5f2f7a719ca9c3d2f4e6fd7a5b27aa6e028c2b5dd8df66fae975bb210b594';
      }
      window.localStorage.setItem(EQ_LOCAL_STORAGE,JSON.stringify(filterChipsStorageObj));
    }
  },
  getFiltersValueFromLocalStorage(type){
    const filterChipsStorageObj={};
    if(type===EQ_TYPE){
      const cacheEquipmentListFiltersObj=JSON.parse(window.localStorage.getItem(EQ_LOCAL_STORAGE));
      if(cacheEquipmentListFiltersObj!==null){
        if (Object.keys(cacheEquipmentListFiltersObj).length){
          const currentUser=storageUtil.getCookie(TPCOOKIEUSERTEM)|| '41d5f2f7a719ca9c3d2f4e6fd7a5b27aa6e028c2b5dd8df66fae975bb210b594';
          if(currentUser===cacheEquipmentListFiltersObj[EQ_FILTERS.USERNAME]){
            filterChipsStorageObj[EQ_FILTERS.COUNTRY] = cacheEquipmentListFiltersObj[EQ_FILTERS.COUNTRY]||'';
            filterChipsStorageObj[EQ_FILTERS.EQUIPMENTNAME] = cacheEquipmentListFiltersObj[EQ_FILTERS.EQUIPMENTNAME]||'';
            filterChipsStorageObj[EQ_FILTERS.SERIALNUMBER] = cacheEquipmentListFiltersObj[EQ_FILTERS.SERIALNUMBER]||'';
          }
          else{
            window.localStorage.removeItem(EQ_LOCAL_STORAGE);
          }
        }
      }
    }
    return filterChipsStorageObj;
  }
};